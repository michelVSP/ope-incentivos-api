package erp.ope.incentivos.ins.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.ins.model.SanctionsIncidents;
import erp.ope.incentivos.ins.repositories.SanctionsIncidentsRepository;

@Service
public class SanctionsIncidentsService 
{
	private final SanctionsIncidentsRepository repository;
	
	public SanctionsIncidentsService(SanctionsIncidentsRepository repository) 
	{
		this.repository = repository;
	}
	
	public Map<String, SanctionsIncidents> buscaSancionesIncidentes()
	{
		List<SanctionsIncidents> lst = repository.findAll();
		Map<String, SanctionsIncidents> map = lst.stream().collect(Collectors.toMap(SanctionsIncidents :: getSanctionCode, e -> e));
		return map;
	}

}
