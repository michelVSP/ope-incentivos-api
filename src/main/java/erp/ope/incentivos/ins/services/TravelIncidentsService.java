package erp.ope.incentivos.ins.services;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.ins.model.TravelIncidents;
import erp.ope.incentivos.ins.repositories.TravelIncidentsRepository;

@Service
public class TravelIncidentsService 
{
	private final TravelIncidentsRepository repository;
	
	public TravelIncidentsService(TravelIncidentsRepository repository) 
	{
		this.repository = repository;
	}
	
	public List<TravelIncidents> buscaIncidentesViajes(LocalDate f1, LocalDate f2)
	{
		Timestamp  fechaIni = Timestamp.valueOf(f1.atStartOfDay());
		Timestamp fechaFin = Timestamp.valueOf(f2.atTime(LocalTime.MAX));
		
		return repository.findByDateIncident(fechaIni, fechaFin);
	}
}
