package erp.ope.incentivos.bpa.services;

import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.BpaTabulator;
import erp.ope.incentivos.bpa.repositories.BpaTabulatorRepository;
import erp.ope.incentivos.exception.RecursoNoEncontradoException;

@Service
public class BpaTabulatorService 
{
	BpaTabulatorRepository repo;
	
	public BpaTabulatorService(BpaTabulatorRepository repo) 
	{
		this.repo = repo;
	}
	
	public List<BpaTabulator> findAllTabulator()
	{
		List<BpaTabulator> lst = repo.findAll();
		if(lst.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron datos de tabuladores de años para calcular el BPA");
		
		return lst;
	}
}
