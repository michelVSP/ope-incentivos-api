package erp.ope.incentivos.ins.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.ins.model.DegreeIncidents;
import erp.ope.incentivos.ins.repositories.DegreeIncidentsRepository;

@Service
public class DegreeIncidentsService
{
	private final DegreeIncidentsRepository repository;
	
	public DegreeIncidentsService(DegreeIncidentsRepository repository) 
	{
		this.repository = repository;
	}
	
	public Map<String, DegreeIncidents> buscaGruposIncidentes()
	{
		List<DegreeIncidents> lst = repository.findAll();
		Map<String, DegreeIncidents> map = lst.stream().collect(Collectors.toMap(DegreeIncidents ::getDegreeCode, e -> e));
		return map;
	}
}
