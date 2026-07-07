package erp.ope.incentivos.bpa.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.DriverEvent;
import erp.ope.incentivos.bpa.repositories.DriverEventRepository;

@Service
public class DriverEventService 
{
	DriverEventRepository repo;
	
	public DriverEventService(DriverEventRepository repo) 
	{
		this.repo = repo;
	}
	
	public List<DriverEvent> findDriverEventsByDriverCodesAndStartDate(String driverCodes, String eventCodes, LocalDate fecStart, LocalDate fecEnd)
	{
		return repo.findByEventCodeAndDriverCodeAndStartTimeBetween(driverCodes, eventCodes, fecStart, fecEnd);
	}
}
