package erp.ope.incentivos.bpa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.BpaDetail;
import erp.ope.incentivos.bpa.repositories.BpaDetailRepository;

@Service
public class BpaDetailService 
{
	BpaDetailRepository repo;
	
	public BpaDetailService(BpaDetailRepository repo)
	{
		this.repo = repo;
	}
	
	public List<BpaDetail> findByYearCalculatedAndDriverCodes(Integer year, List<String> codes)
	{
		return repo.findByYearCalculatedAndDriverCodeIn(year, codes);
	}
}
