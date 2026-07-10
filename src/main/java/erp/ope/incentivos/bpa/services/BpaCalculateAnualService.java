package erp.ope.incentivos.bpa.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.dto.DriverCalculateResponse;
import erp.ope.incentivos.bpa.dto.ResumeResponse;
import erp.ope.incentivos.bpa.model.BpaDetail;
import erp.ope.incentivos.bpa.model.BpaTabulator;
import erp.ope.incentivos.bpa.model.Driver;
import erp.ope.incentivos.bpa.model.DriverEvent;
import erp.ope.incentivos.bpa.model.DriverIncentive;
import erp.ope.incentivos.bpa.model.pk.DriverIncentivePK;
import erp.ope.incentivos.exception.RecursoNoEncontradoException;

@Service
public class BpaCalculateAnualService 
{
	DriverService driverServ;
	BpaParamService bpaParamService;
	DriverEventService driverEventService;
	BpaDetailService bpaDetailServ;
	BpaTabulatorService bpaTabulatorServ;
	DriverIncentiveService driverIncentService;
	
	public BpaCalculateAnualService(BpaParamService bpaParamService, 
									DriverEventService driverEventService, 
									DriverService driverServ, 
									BpaDetailService bpaDetailServ,
									BpaTabulatorService bpaTabulatorServ,
									DriverIncentiveService driverIncentService) 
	{
		this.bpaParamService = bpaParamService;
		this.driverEventService = driverEventService;
		this.driverServ = driverServ;
		this.bpaDetailServ = bpaDetailServ;
		this.bpaTabulatorServ = bpaTabulatorServ;
		this.driverIncentService = driverIncentService;
	}
	
	public ResumeResponse calculateBPAOnAbsoluteDate(List<String> lstDriverCodesStr, LocalDate dateCalculate) 
	{
		/**
		 * busquedas de parametros de 
		 * - Eventos de kardex
		 * - parametro de dias minimos de trabajo 
		 * - parametros de intervalos de años para monto de dias
		 * */
		String eventCodes = bpaParamService.findParamIncidentsBpa();
		String minDaysGoalStr = bpaParamService.finParamMinimunDaysToBPA();
		List<BpaTabulator> lstBpaTabulatorServ = bpaTabulatorServ.findAllTabulator();
		int minDaysGoal = Integer.parseInt(minDaysGoalStr);
		
		/**
		 * Se obtienen conductores despues del filtrado y validaciones para realizar calculo del bono 
		 * */
		List<Driver> lstDrivers = getDriversCalculate(lstDriverCodesStr, dateCalculate);
		
		/**
		 * se buscan las incidencias de los conductores a evaluar 
		 */
		Map<String, List<DriverEvent>> mapIncid = findDriverEvents(lstDrivers, eventCodes, dateCalculate);
		
		/**
		 * proceso de calculo de bono
		 * */
		List<List<Object>> lst = calculateBPA(lstDrivers, minDaysGoal, mapIncid, dateCalculate, lstDriverCodesStr, lstBpaTabulatorServ);
		List<DriverIncentive> lstDriverIncentives = lst.get(0).stream().map(e -> (DriverIncentive) e).toList();
		List<BpaDetail> lstBpaDetails = lst.get(1).stream().map(e -> (BpaDetail) e).toList();
		
		/**
		 * Guardo de informacion ( Driver incentive - BPADetail
		 * */
		driverIncentService.saveDriversBPAIncentive(lstDriverIncentives);
		bpaDetailServ.saveBpaDetails(lstBpaDetails);
		
		/**
		 * Respues al cliente con el resumen del proceso
		 * */
		return getDriverResponse(lstDriverCodesStr, lstDriverIncentives, dateCalculate, minDaysGoalStr);
	}
	
	private ResumeResponse getDriverResponse(List<String> lstDriverCodesStr, List<DriverIncentive> lstDriverIncentives, LocalDate dateCalculate, String minDaysGoalStr) 
	{
		ResumeResponse vo = new ResumeResponse();
		vo.setDateProcess(dateCalculate);
		vo.setTotalRequested(lstDriverCodesStr.size());
		vo.setProcessed(lstDriverIncentives.size());
		vo.setSkipped(lstDriverCodesStr.size() - lstDriverIncentives.size());

		List<String> lstDriverGain = new ArrayList<>();
		List<DriverCalculateResponse> lst = new ArrayList<>();
		for (DriverIncentive driverIncentive : lstDriverIncentives) 
		{
			DriverCalculateResponse dResp = new DriverCalculateResponse();
			dResp.setBonusDays(driverIncentive.getAmount());
			dResp.setDriverCode(driverIncentive.getPk().getDriverCode());	
			dResp.setMinDaysRequired(Integer.parseInt(minDaysGoalStr));
			dResp.setStatusGl(driverIncentive.getStatusIncentive());
			dResp.setTotalDays(driverIncentive.getTotalDays());
			lst.add(dResp);
			
			if(driverIncentive.getStatusIncentive().intValue() == 1)
				vo.setGained(vo.getGained().intValue() + 1);
			else
				vo.setLost(vo.getLost().intValue() + 1);
			
			lstDriverGain.add(driverIncentive.getPk().getDriverCode());
		}
		vo.setResults(lst);
		
		List<String> lstDriverSkiped = lstDriverCodesStr.stream().filter(e -> !lstDriverGain.contains(e)).toList();
		vo.setSkippedDriverCodes(lstDriverSkiped);
		
		return vo;
	}

	private List<List<Object>> calculateBPA(List<Driver> lstDrivers, int minDaysGoal, Map<String, List<DriverEvent>> mapIncid, LocalDate dateCalculate, List<String> lstDriverCodesStr, List<BpaTabulator> lstBpaTabulatorServ) 
	{
		List<List<Object>> lst = new ArrayList<>();
		
		List<Object> lstDriversIncentives = new ArrayList<>();
		List<Object> lstDriversBpaDetail = new ArrayList<>();
		
		for (Driver driver : lstDrivers) 
		{
			List<DriverEvent> lstEvent = mapIncid.get(driver.getDriverCode());
			if(lstEvent == null || lstEvent.isEmpty())
				continue;
			
			int daysWork = sumDaysEvents(lstEvent);
			
			DriverIncentive vo = driverToDriverIncentive(driver);
			vo.getPk().setYearCalculated(dateCalculate.getYear());
			vo.setTotalDays(daysWork);
			
			BpaDetail voDet = driverToBpaDetail(driver, dateCalculate);
			voDet.setDaysIncentivesAnalyzed(daysWork);
			voDet.setIncentivesAnalyzed(lstEvent.size());
			
			if(daysWork >= minDaysGoal)
			{
				Integer amount = getAmountByParameterTabulator(driver, lstBpaTabulatorServ, dateCalculate);
				vo.setStatusIncentive(1);
				vo.setAmount(amount);
				voDet.setStatus(1);
			}
			
			lstDriversIncentives.add(vo);
			lstDriversBpaDetail.add(voDet);
		}
		
		lst.add(lstDriversIncentives);
		lst.add(lstDriversBpaDetail);
		
		return lst;
	}

	private BpaDetail driverToBpaDetail(Driver driver, LocalDate dateCalculate) 
	{
		BpaDetail vo = new BpaDetail();
		vo.setBrandCode(driver.getBrandCode());
		vo.setBrandEntryDate(driver.getBrandEntryDate());
		vo.setBrandName(driver.getBrandName());
		vo.setDriverCode(driver.getDriverCode());
		vo.setDriverName(driver.getFirstName() +" " + driver.getPaternalLastName() +" " + driver.getMaternalLastName());
		vo.setRegionEntryDate(driver.getRegionEntryDate());
		vo.setStatus(0);
		vo.setCalculatedAt(dateCalculate);
		vo.setYearCalculated(dateCalculate.getYear());
		vo.setEmployeSeniority( (int) ChronoUnit.YEARS.between(driver.getRegionEntryDate(), dateCalculate));
		
		return vo;
	}

	private DriverIncentive driverToDriverIncentive(Driver driver) 
	{	
		DriverIncentive vo = new DriverIncentive();
		DriverIncentivePK pk = new DriverIncentivePK();
		
		pk.setDriverCode(driver.getDriverCode());
		pk.setIncentiveCode("BPA");
		pk.setMonthCalculated(1); // por defaul 1 ya que solo es una ves al año
		vo.setPk(pk);
		vo.setFrecuency("ANUAL");
		vo.setIncentiveDescription("BONO ANUAL DE PRODUCTIVIDAD");
		vo.setStatusIncentive(0);
		vo.setAmount(0);
		return vo;
	}

	private Integer getAmountByParameterTabulator(Driver driver, List<BpaTabulator> lstBpaTabulatorServ, LocalDate dateCalculate) 
	{
	    int years = (int) ChronoUnit.YEARS.between(driver.getRegionEntryDate(), dateCalculate);

	    return lstBpaTabulatorServ.stream()
	            .filter(vo -> vo.getPk().getMinimumSeniority().intValue() <= years 
	                       && vo.getPk().getMaximumSeniority().intValue() >= years)
	            .map(BpaTabulator::getBonus_Days)
	            .findFirst()
	            .orElse(null);
	}

	private int sumDaysEvents(List<DriverEvent> lstEvent) 
	{
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		List<String> lstdates = new ArrayList<>();
		
		for (DriverEvent vo : lstEvent) 
		{
			LocalDate fec = vo.getStartTime();
			while(fec.isBefore(vo.getEndTime()) || fec.isEqual(vo.getEndTime()))
			{
				if(!lstdates.contains(fec.format(formato)))
					lstdates.add(fec.format(formato));
				fec = fec.plusDays(1);
			}
		}
		
		return lstdates.size();
	}

	public List<Driver> getDriversCalculate(List<String> lstDriverCodesStr, LocalDate dateCalculate)
	{
		/**
		 * Validamos que existan conductores activos
		 **/
		List<Driver> lstDrivers = driverServ.findDriversByCveIn(lstDriverCodesStr);
		if(lstDrivers.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron conductores activos");
		
		/**
		 * validamos que la fecha de aniversario de calculo concuerde con la fecha (mes-dia) de ingreso a la region de cada conductor
		 **/
		List<Driver> lstDriverAniv= validateDriverAniversary(lstDrivers, dateCalculate);
		if(lstDriverAniv.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron conductores que cumplan aniversario en la fecha proporcionada");
		
		/**
		 * buscamos calculos previos en el año de los conductores filtrados en el año
		 * */
		List<String> driverCodes = lstDriverAniv.stream().map(e -> e.getDriverCode()).collect(Collectors.toList());
		List<BpaDetail> lstBpaDetail = bpaDetailServ.findByYearCalculatedAndDriverCodes(dateCalculate.getYear(), driverCodes);
		
		/**
		 * Validamos que los conductores filtrados no hayan tenido calculos previos, de ser asi se excliran del proceso
		 * */
		List<String> lstDriverCodesBpaDetail = lstBpaDetail.stream().filter(e -> e!= null && e.getDriverCode() != null).map(BpaDetail::getDriverCode).toList();
		List<Driver> lstDriversCalculate = lstDriverAniv.stream() .filter(vo -> !lstDriverCodesBpaDetail.contains(vo.getDriverCode())).toList();

		return lstDriversCalculate;
	}

	private Map<String, List<DriverEvent>> findDriverEvents(List<Driver> lstDrivers, String eventCodes, LocalDate fecEnd)
	{
		List<String> driverCodes = lstDrivers.stream().map(e -> e.getDriverCode()).collect(Collectors.toList());
		List<String> listEventCodes = Arrays.asList(eventCodes.split(","));
		
		LocalDate fecStart = fecEnd.minusYears(1);
		
		List<DriverEvent> lstDrivEvent = driverEventService.findDriverEventsByDriverCodesAndStartDate(driverCodes, listEventCodes, fecStart, fecEnd);
		
		return lstDrivEvent.stream().collect(Collectors.groupingBy(DriverEvent::getDriverCode));
	}
	
	private List<Driver> validateDriverAniversary(List<Driver> lstDrivers, LocalDate dateCalculate) 
	{
	    java.time.MonthDay fDateCal = java.time.MonthDay.from(dateCalculate);
	    
	    return lstDrivers.stream().filter(driver -> 
	    {
	    	LocalDate regionEntry = driver.getRegionEntryDate();
	    	if (regionEntry == null) 
	    		return false; 
	                
	    	return java.time.MonthDay.from(regionEntry).equals(fDateCal);
	    }).collect(Collectors.toList());
	}
}
