package erp.ope.incentivos.bpa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.DriverIncentive;
import erp.ope.incentivos.bpa.repositories.DriverIncentiveRepository;

@Service
public class DriverIncentiveService 
{
	DriverIncentiveRepository repo;
	
	public DriverIncentiveService(DriverIncentiveRepository repo) 
	{
		this.repo = repo;
	}

	public void saveDriversBPAIncentive(List<DriverIncentive>  lstDriverIncentives)
	{
			repo.saveAll(lstDriverIncentives);
	}
	
}
