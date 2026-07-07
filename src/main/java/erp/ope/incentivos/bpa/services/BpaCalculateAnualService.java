package erp.ope.incentivos.bpa.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.BpaDetail;
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
	
	public BpaCalculateAnualService(BpaParamService bpaParamService, DriverEventService driverEventService, 
									DriverService driverServ, BpaDetailService bpaDetailServ) 
	{
		this.bpaParamService = bpaParamService;
		this.driverEventService = driverEventService;
		this.driverServ = driverServ;
		this.bpaDetailServ = bpaDetailServ;
	}
	
	public void calculateBPAOnAbsoluteDate(List<String> lstDriverCodesStr, LocalDate dateCalculate) 
	{
		/**
		 * Se obtienen conductores despues del filtrado y validaciones para realizar calculo del bono 
		 * */
		List<Driver> lstDrivers = getDriversCalculate(lstDriverCodesStr, dateCalculate);
		
		/**
		 * busca parametro de filtrado de incidencias y parammetro de dias minimos de trabajo para obtener el bono
		 * */
		
		String eventCodes = bpaParamService.findParamIncidentsBpa();
		String minDaysGoalStr = bpaParamService.finParamMinimunDaysToBPA();
		int minDaysGoal = Integer.parseInt(minDaysGoalStr);
		
		/**
		 * se buscan las incidencias de los conductores a evaluar 
		 */
		Map<String, List<DriverEvent>> mapIncid = findDriverEvents(lstDrivers, eventCodes, dateCalculate);
		
		/**
		 * proceso de calculo de bono
		 * */
		calculateBPA(lstDrivers, minDaysGoal, mapIncid, dateCalculate);		
		
	}
	
	private void calculateBPA(List<Driver> lstDrivers, int minDaysGoal, Map<String, List<DriverEvent>> mapIncid, LocalDate dateCalculate) 
	{
		List<DriverIncentive> lstDriversIncentives = new ArrayList<>();
		
		for (Driver driver : lstDrivers) 
		{
			List<DriverEvent> lstEvent = mapIncid.get(driver.getDriverCode());
			if(lstEvent == null || lstEvent.isEmpty())
				continue;
			
			int daysWork = sumDaysEvents(lstEvent);
			
			DriverIncentive vo = new DriverIncentive();
			DriverIncentivePK pk = new DriverIncentivePK();
			
			pk.setDriverCode(driver.getDriverCode());
			pk.setIncentiveCode("BPA");
			pk.setMonthCalculated(1); // por defaul 1 ya que solo es una ves al año
			pk.setYearCalculated(dateCalculate.getYear());
			vo.setPk(pk);
			vo.setFrecuency("ANUAL");
			vo.setIncentiveDescription("BONO ANUAL DE PRODUCTIVIDAD");
			vo.setStatusIncentive(0);
			vo.setAmount(0);
			
			if(daysWork >= minDaysGoal)
			{
				vo.setStatusIncentive(1);
				vo.setAmount(1);
			}
			
			lstDriversIncentives.add(vo);
		}
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
				fec.plusDays(1);
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
		String driverCodes = lstDriverAniv.stream().map(Driver :: getDriverCode).collect(Collectors.joining(","));
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
		String driverCodes = lstDrivers.stream().map(e -> e.getDriverCode()).collect(Collectors.joining(","));
		
		LocalDate fecStart = fecEnd.minusYears(1);
		
		List<DriverEvent> lstDrivEvent = driverEventService.findDriverEventsByDriverCodesAndStartDate(driverCodes, eventCodes, fecStart, fecEnd);
		
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
