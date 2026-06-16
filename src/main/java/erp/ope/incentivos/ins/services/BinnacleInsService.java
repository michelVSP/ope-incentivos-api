package erp.ope.incentivos.ins.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.exception.RecursoNoEncontradoException;
import erp.ope.incentivos.ins.model.BinnacleIns;
import erp.ope.incentivos.ins.repositories.BinnacleInsRepository;

@Service
public class BinnacleInsService 
{
	static final Integer STATUS_NOMINA_CALCULADO = 2;

	private final BinnacleInsRepository repository;
	
	public BinnacleInsService(BinnacleInsRepository repository) 
	{
		this.repository = repository;
	}
	
	public boolean buscaEstatusNominaExistente(LocalDate fechaIni, LocalDate fechaFin) 
	{
		List<BinnacleIns> lst = repository.findByRosterStatusAndDateStartBetween(STATUS_NOMINA_CALCULADO, fechaIni, fechaFin);
		if(lst.isEmpty())
			return true;
		
		return false;
	}
	
	public void limpiaBitacoraIns(LocalDate fechaIni, LocalDate fechaFin) throws Exception
	{
		repository.deleteByDateStartBetween(fechaIni, fechaFin);
	}
	
	public List<BinnacleIns> buscaBitacorasXCuatrimestre(LocalDate fechaIni, LocalDate fechaFin) 
	{
		List<BinnacleIns> lst = repository.findByDateStartBetween(fechaIni, fechaFin);
		
		if(lst == null || lst.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron datos con los parametros seleccionados");
		
		return lst;
	}
}
