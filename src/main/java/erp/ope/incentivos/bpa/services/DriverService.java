package erp.ope.incentivos.bpa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.Driver;
import erp.ope.incentivos.bpa.repositories.DriverRepository;
import erp.ope.incentivos.exception.BadRequestException;

@Service
public class DriverService 
{
	DriverRepository repo;
	
	public DriverService(DriverRepository repo) 
	{
		this.repo = repo;
	}
	
	public List<Driver> findAllDriversActives()
	{
		return repo.findAllByHrStatus(1);
	}

	public List<Driver> findDriversByCveIn(List<String> lstDriverCodes)
	{
		if(lstDriverCodes == null || lstDriverCodes.isEmpty())
			throw new BadRequestException("No se dieron claves de conductores para buscar");

		return repo.findByDriverCodesIn(lstDriverCodes);
	}
}
