package erp.ope.incentivos.ins.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import erp.ope.incentivos.ins.model.InsDetail;
import erp.ope.incentivos.ins.model.TravelIncidents;
import erp.ope.incentivos.ins.repositories.InsDetailRepository;

@Service
public class InsDetailService 
{
	private final InsDetailRepository repository;
	private final TravelIncidentsService travelIncidService;
	
	public InsDetailService(InsDetailRepository repository, TravelIncidentsService travelIncidService) 
	{
		this.repository = repository;
		this.travelIncidService = travelIncidService;
	}
	
	public void limpiaDetallesInsBitacora(LocalDate fechaIni, LocalDate fechaFin) 
	{
		repository.deleteByDateStartBetween(fechaIni, fechaFin);
	}
	
	public Map<String, List<InsDetail>> buscaDetallesXPeriodoXAnio(LocalDate fechaIni, LocalDate fechaFin) 
	{
		List<TravelIncidents> lst = travelIncidService.buscaIncidentesViajes(fechaIni, fechaFin);
		
		Map<String, List<InsDetail>> map = lst.stream()
			    .collect(Collectors.groupingBy(TravelIncidents::getDriverCode,
			        Collectors.mapping(e -> travelIncidentToInsDetail(e), Collectors.toList())));
		
		return map;
	}
	
	public InsDetail travelIncidentToInsDetail(TravelIncidents incid) 
	{
		InsDetail vo = new InsDetail();
		vo.setDateIncident(incid.getDateIncident());
		vo.setDateSanctioned(incid.getDateSanctioned());
		vo.setDegreeCode(incid.getDegreeCode());
		vo.setDegreeDescription(incid.getDegreeDescription());
		vo.setDriverCode(incid.getDriverCode());
		vo.setHourIncident(incid.getHourIncident());
		vo.setSanctionCode(incid.getSanctionCode());
		vo.setSanctiondescription(incid.getSanctionDescription());
		return vo;
	}
}
