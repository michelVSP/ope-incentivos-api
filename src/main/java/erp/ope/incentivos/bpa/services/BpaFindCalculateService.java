package erp.ope.incentivos.bpa.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.dto.DriverResponse;
import erp.ope.incentivos.bpa.model.BpaDetail;
import erp.ope.incentivos.bpa.model.Driver;
import erp.ope.incentivos.exception.RecursoNoEncontradoException;

@Service
public class BpaFindCalculateService 
{
	DriverService driverServ;
	BpaDetailService bpaDetailServ;
	
	public BpaFindCalculateService(DriverService driverServ, BpaDetailService bpaDetailServ)
	{
		this.driverServ = driverServ;
		this.bpaDetailServ = bpaDetailServ;
	}
	
	public List<DriverResponse> findDriversAniversary(LocalDate dateCalculate) 
	{
		/**
		 * Validamos que existan conductores activos
		 **/
		List<Driver> lstDrivers = driverServ.findAllDriversActives();
		if(lstDrivers.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron conductores activos");
		
		/**
		 * validamos que la fecha de aniversario de calculo concuerde con la fecha (mes-dia) de ingreso a la region de cada conductor
		 **/
		Map<String, Driver> mapDriverAniv= validateDriverAniversary(lstDrivers, dateCalculate);
		if(mapDriverAniv.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron conductores que cumplan aniversario en la fecha proporcionada");
		
		/**
		 * buscamos calculos previos en el año de los conductores filtrados en el año
		 * */
		List<String> driverCodes = mapDriverAniv.keySet().stream().map(e -> "'"+ e +"'").collect(Collectors.toList());
		List<BpaDetail> lstDetail = bpaDetailServ.findByYearCalculatedAndDriverCodes(dateCalculate.getYear(), driverCodes);
		List<String> lstDriverCodes = lstDetail.stream().filter(e -> e!= null && e.getDriverCode() != null).map(BpaDetail::getDriverCode).toList();
		
		/**
		 * Se genera por ultimo la respuesta al front con DTO
		 * */
		List<DriverResponse> response = generateDriversResponse(mapDriverAniv, lstDriverCodes, dateCalculate);
		return response;
	}
	
	private List<DriverResponse> generateDriversResponse(Map<String, Driver> mapDriverAniv,	List<String> lstDriverCodes, LocalDate dateCalculate) 
	{
		List<DriverResponse> resp = new ArrayList<>();
		
		for (Driver driver : mapDriverAniv.values()) 
		{
			DriverResponse vo = toDriverResponse(driver);
			if(lstDriverCodes.contains(vo.getDriverCode()))
				vo.setAlreadyCalculated(true);
			
			long dif = ChronoUnit.YEARS.between(driver.getRegionEntryDate(), dateCalculate);
			Integer years = (int) dif;
			vo.setYearsOfService(years);
			
			resp.add(vo);
		}
		
		return resp;
	}
	
	private DriverResponse toDriverResponse(Driver driver) 
	{
		DriverResponse vo = new DriverResponse();
		vo.setDriverCode(driver.getDriverCode());
		vo.setFirstName(driver.getFirstName());
		vo.setPaternalLastName(driver.getPaternalLastName());
		vo.setMaternalLastName(driver.getMaternalLastName());
		vo.setRegionEntryDate(driver.getRegionEntryDate());
		
		return vo;
	}

	private Map<String, Driver> validateDriverAniversary(List<Driver> lstDrivers, LocalDate dateCalculate) 
	{
	    java.time.MonthDay fDateCal = java.time.MonthDay.from(dateCalculate);
	    
	    return lstDrivers.stream().filter(driver -> 
	    {
	    	LocalDate regionEntry = driver.getRegionEntryDate();
	    	if (regionEntry == null) 
	    		return false; 
	                
	    	return java.time.MonthDay.from(regionEntry).equals(fDateCal);
	    }).collect(Collectors.toMap(Driver :: getDriverCode , Function.identity()));
	}
	
}
