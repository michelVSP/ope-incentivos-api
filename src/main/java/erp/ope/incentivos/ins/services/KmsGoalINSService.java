package erp.ope.incentivos.ins.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.exception.BadRequestException;
import erp.ope.incentivos.ins.model.KmsGoalINS;
import erp.ope.incentivos.ins.repositories.KmsGoalINSRepository;

@Service
public class KmsGoalINSService 
{
	private final KmsGoalINSRepository repository;
	
	public KmsGoalINSService(KmsGoalINSRepository repository) 
	{
		this.repository = repository;
	}
	
	public Map<String, KmsGoalINS> buscaParmetrosKilometros(Integer anio, String cuatrimestre)
	{
		Integer periodo = 0;
		
		if(cuatrimestre.trim().equalsIgnoreCase("PRIMER CUATRIMESTRE"))
			periodo = 1;
		if(cuatrimestre.trim().equalsIgnoreCase("SEGUNDO CUATRIMESTRE"))
			periodo = 2;
		if(cuatrimestre.trim().equalsIgnoreCase("TERCER CUATRIMESTRE"))
			periodo = 3;
		
		if(periodo == 0)
			throw new BadRequestException("Descripcion de cuatrimestre invalido");
		
		List<KmsGoalINS> lst = repository.findByYearAndPeriodo(anio, periodo);
		Map<String, KmsGoalINS> map = lst.stream().collect(Collectors.toMap(e -> e.getPk().getPlazaCobro() +"-"+ e.getPk().getBrandCode()+"-"+ e.getPk().getZoneCode()+"-"+ e.getPk().getPeriod()+"-"+ e.getPk().getYearPeriod(), e -> e));
		return map;
	}
}
