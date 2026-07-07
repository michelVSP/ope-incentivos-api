package erp.ope.incentivos.bpa.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.bpa.model.BpaParam;
import erp.ope.incentivos.bpa.model.pk.BpaParamPK;
import erp.ope.incentivos.bpa.repositories.BpaParamRepository;
import erp.ope.incentivos.exception.RecursoNoEncontradoException;

@Service
public class BpaParamService 
{
	BpaParamRepository repo;
	
	public BpaParamService(BpaParamRepository repo) 
	{
		this.repo = repo;
	}
	
	public String findParamIncidentsBpa()
	{
		BpaParamPK pk = new BpaParamPK();
		pk.setIncentiveCode("BPA");
		pk.setParamName("BPA_TYPE_INCENTIVES_CODE");
		
		Optional<BpaParam> opc = repo.findById(pk);
		if(opc.isPresent())
			throw new RecursoNoEncontradoException("No se encontro el paramtro de incidencias");
		
		BpaParam vo = opc.get();
		if(vo.getParamValue() == null || vo.getParamValue().trim().equalsIgnoreCase(""))
			throw new RecursoNoEncontradoException("No se dio un valor al parametro BPA_TYPE_INCENTIVES_CODE");
			
		return vo.getParamValue();
	}

	public String finParamMinimunDaysToBPA() 
	{
		BpaParamPK pk = new BpaParamPK();
		pk.setIncentiveCode("BPA");
		pk.setParamName("BPA_MIN_DAYS_GOAL");
		
		Optional<BpaParam> opc = repo.findById(pk);
		if(opc.isPresent())
			throw new RecursoNoEncontradoException("No se encontro el paramtro de minimo de dias para alcanzar el bono");
		
		BpaParam vo = opc.get();
		if(vo.getParamValue() == null || vo.getParamValue().trim().equalsIgnoreCase(""))
			throw new RecursoNoEncontradoException("No se dio un valor al parametro BPA_MIN_DAYS_GOAL");
			
		return vo.getParamValue();
	}
}
