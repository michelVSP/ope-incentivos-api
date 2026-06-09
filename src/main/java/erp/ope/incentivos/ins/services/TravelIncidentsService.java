package erp.ope.incentivos.ins.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.ins.model.TravelIncidents;
import erp.ope.incentivos.ins.repositories.TravelIncidentaRepository;

@Service
public class TravelIncidentsService 
{
	private final TravelIncidentaRepository repository;
	
	public TravelIncidentsService(TravelIncidentaRepository repository) 
	{
		this.repository = repository;
	}
	
	public List<TravelIncidents> buscaIncidentesViajes(LocalDate f1, LocalDate f2)
	{
		Timestamp  fechaIni = Timestamp.valueOf(f1.atStartOfDay());
		Timestamp fechaFin = Timestamp.valueOf(f2.atStartOfDay());
		
		return repository.findByDateIncident(fechaIni, fechaFin);
	}
}
