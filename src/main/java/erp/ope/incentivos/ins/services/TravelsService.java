package erp.ope.incentivos.ins.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.ins.model.BinnacleIns;
import erp.ope.incentivos.ins.repositories.TravelRepository;

@Service
public class TravelsService 
{
	private final TravelRepository repository;
	
	public TravelsService(TravelRepository repository) 
	{
		this.repository = repository;
	}

	public List<BinnacleIns> consultaAcumuladoPorCuatrimestre(String regionId, String marcaId, String zonaId, LocalDate fechaIni, LocalDate fechaFin) 
	{
		return repository.consultaAcumuladoPorCuatrimestre(regionId, marcaId, zonaId, fechaIni, fechaFin);
	}
}
