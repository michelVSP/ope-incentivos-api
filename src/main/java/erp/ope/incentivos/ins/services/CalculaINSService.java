package erp.ope.incentivos.ins.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import erp.ope.incentivos.exception.BadRequestException;
import erp.ope.incentivos.exception.CalculoDuplicadoException;
import erp.ope.incentivos.exception.RecursoNoEncontradoException;
import erp.ope.incentivos.ins.dto.BinnacleInsResponse;
import erp.ope.incentivos.ins.dto.InsCalculateResponse;
import erp.ope.incentivos.ins.dto.InsDetailResponse;
import erp.ope.incentivos.ins.model.BinnacleIns;
import erp.ope.incentivos.ins.model.DegreeIncidents;
import erp.ope.incentivos.ins.model.InsDetail;
import erp.ope.incentivos.ins.model.KmsGoalINS;
import erp.ope.incentivos.ins.model.SanctionsIncidents;

@Service
public class CalculaINSService 
{
	private final DegreeIncidentsService degreeIncidServ;
	private final BinnacleInsService binnacleInsServ;
	private final InsDetailService insDetailService;
	private final SanctionsIncidentsService sanctionsIncidService;
	private final KmsGoalINSService kmsGoalINSService;
	private final TravelsService travelsService;
	
	public CalculaINSService(DegreeIncidentsService degreeIncidServ, 
								BinnacleInsService binnacleInsServ, 
								InsDetailService insDetailService,
								SanctionsIncidentsService sanctionsIncidService,
								KmsGoalINSService kmsGoalINSService,
								TravelsService travelsService) 
	{	
		this.degreeIncidServ = degreeIncidServ;
		this.binnacleInsServ = binnacleInsServ;
		this.insDetailService = insDetailService;
		this.sanctionsIncidService = sanctionsIncidService;
		this.kmsGoalINSService = kmsGoalINSService;
		this.travelsService = travelsService;
	}

	public InsCalculateResponse procesaCalculoINS(String cuatrimestre, Integer anio) 
	{
		if(cuatrimestre == null)
			throw new BadRequestException("Periodo invalido");
		
		if(anio == null || (anio.toString().length() != 4))
			throw new BadRequestException("Año invalido");
		
		boolean continuar = validaCalculoExistente(cuatrimestre, anio);
		if(!continuar)
			throw new CalculoDuplicadoException("El proceso de pago ya ha sido ejecutado por nómina, no es posible hacer el cálculo nuevamente.");
		
		return procesaCalculoInsCuatrimestral(cuatrimestre, anio);
	}

	@Transactional
	public InsCalculateResponse procesaCalculoInsCuatrimestral(String cuatrimestre, Integer anio) 
	{
		InsCalculateResponse vo = new InsCalculateResponse();
		vo.setPeriod(cuatrimestre);
		vo.setYear(anio);
		
		LocalDate  fechaIni = obtieneFechaIni(cuatrimestre, anio);
		LocalDate  fechaFin = obtieneFechaFin(cuatrimestre, anio);
		
		////  limpia Bitacoras anteriores no pagadas por nomina
		limpiaInsBitacoraYDetalle(fechaIni, fechaFin);
		
		/////  carga parametros
		Map<String, DegreeIncidents> mapDreegreInc = degreeIncidServ.buscaGruposIncidentes();
		Map<String, SanctionsIncidents> mapSanctions  = sanctionsIncidService.buscaSancionesIncidentes();
		Map<String, KmsGoalINS> mapKmsGOAL = kmsGoalINSService.buscaParmetrosKilometros(anio, cuatrimestre);
		
		/////  busca detalles INS
		Map<String, List<InsDetail>> mapINSDetail = insDetailService.buscaDetallesXPeriodoXAnio(fechaIni, fechaFin);
	
		Map<String, BinnacleIns> bitacoras = new HashMap<>();
		
		/////  busca kms viajes nomina por ambito y relaciona
		mapKmsGOAL.values().stream().forEach(kmsGOAL -> 
		{
			String regionId = kmsGOAL.getPk().getPlazaCobro();
			String marcaId = kmsGOAL.getPk().getBrandCode();
			String zonaId = kmsGOAL.getPk().getZoneCode();
			
			Map<String, BinnacleIns> acumulados = consultaAcumuladoPorCuatrimestre(regionId, marcaId, zonaId , fechaIni, fechaFin);
			
			relacionaViajesAcomuladosVSIncidencia(acumulados, mapINSDetail, fechaIni, fechaFin);
			validaIncentivoINS(acumulados, mapDreegreInc, mapSanctions, kmsGOAL, vo);
			bitacoras.putAll(acumulados);
		});
		
		/////  guarda resultado evaluacion de incentivo
		guardaEnInsBitacoraInsDetalle(bitacoras);
		
		vo.setTotalDrivers(bitacoras.size());
		vo.setProcessed(bitacoras.size());
		
		return vo;
	}

	@Transactional
	public void guardaEnInsBitacoraInsDetalle(Map<String, BinnacleIns> acumulados) 
	{
		binnacleInsServ.guardaBitacoraInsDetalle(acumulados);
	}

	public void validaIncentivoINS(Map<String, BinnacleIns> acumulados, Map<String, DegreeIncidents> mapDreegreInc,
									Map<String, SanctionsIncidents> mapSanctions, KmsGoalINS e, InsCalculateResponse vo) 
	{
		for (BinnacleIns bitacora : acumulados.values()) 
		{
			boolean estatusGP = validaAlcanzaIncentivoINS(bitacora, e.getKms(), mapDreegreInc, mapSanctions);
			if(estatusGP)
			{
				bitacora.setStatusGP(1);
				vo.setEligible(vo.getEligible() + 1);
			}
			else
			{
				bitacora.setStatusGP(0);
				vo.setIneligible(vo.getIneligible() + 1);
			}
		}
	}
	
	public boolean validaAlcanzaIncentivoINS(BinnacleIns bitacora, Integer kmsMeta, Map<String, DegreeIncidents> mapDreegreInc, Map<String, SanctionsIncidents> mapSanctions) 
	{
		if(bitacora.getKilometers().intValue() < kmsMeta.intValue()) 
			return false; 

		if(bitacora.getLstInsDetails() == null || bitacora.getLstInsDetails().isEmpty()) 
			return true;

		boolean tieneAfectacion = bitacora.getLstInsDetails().stream().map(x -> mapDreegreInc.get(x.getDegreeCode()))
															.filter(Objects::nonNull)
															.anyMatch(y -> Integer.valueOf(1).equals(y.getAffectationINS()));
		if(tieneAfectacion)
			return false;
		
		tieneAfectacion = bitacora.getLstInsDetails().stream().map(x -> mapSanctions.get(x.getSanctionCode()))
													.filter(Objects::nonNull)
													.anyMatch(y -> Integer.valueOf(1).equals(y.getAffectationINS()));
		if(tieneAfectacion)
			return false;
		
		return true;
	}

	public void relacionaViajesAcomuladosVSIncidencia(Map<String, BinnacleIns> acumulados, Map<String, List<InsDetail>> mapINSDetail, LocalDate  fechaIni, LocalDate fechaFin) 
	{
		for (BinnacleIns bitacora : acumulados.values()) 
		{
			bitacora.setDateStart(fechaIni);
			bitacora.setDateEnd(fechaFin);
			bitacora.setDateCalculation(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			bitacora.setRosterStatus(1);
			
			List<InsDetail> lstDetalles = mapINSDetail.get(bitacora.getDriverCode());
			if(lstDetalles == null || lstDetalles.isEmpty())
				continue;
			
			bitacora.setLstDetails(lstDetalles);
		}
	}

	public Map<String, BinnacleIns> consultaAcumuladoPorCuatrimestre(String regionId, String marcaId, String zonaId, LocalDate fechaIni, LocalDate fechaFin) 
	{
		List<BinnacleIns> lst = travelsService.consultaAcumuladoPorCuatrimestre(regionId, marcaId, zonaId, fechaIni, fechaFin);
		Map<String, BinnacleIns> map = lst.stream().collect(Collectors.toMap(BinnacleIns :: getDriverCode, e ->e));
		return map;
	}

	public void limpiaInsBitacoraYDetalle(LocalDate fechaIni, LocalDate fechaFin) 
	{
		insDetailService.limpiaDetallesInsBitacora(fechaIni, fechaFin);
		binnacleInsServ.limpiaBitacoraIns(fechaIni, fechaFin);
	}

	public boolean validaCalculoExistente(String cuatrimestre, Integer anio) 
	{
		LocalDate  fechaIni = obtieneFechaIni(cuatrimestre, anio);
		LocalDate  fechaFin = obtieneFechaFin(cuatrimestre, anio);
		
		return binnacleInsServ.buscaEstatusNominaExistente(fechaIni, fechaFin);
	}
	
	public LocalDate obtieneFechaIni(String cuatrimestre, Integer anio)
	{
		int mesInicio = -1;
		
		if(cuatrimestre.equalsIgnoreCase("PRIMER CUATRIMESTRE")) 
			mesInicio = 0;
		
		else if(cuatrimestre.equalsIgnoreCase("SEGUNDO CUATRIMESTRE")) 
			mesInicio = 4;
		
		else if(cuatrimestre.equalsIgnoreCase("TERCER CUATRIMESTRE")) 
			mesInicio = 8;
		else
			throw new BadRequestException("Descripcion de cuatrimestre invalido");
		
		return LocalDate.of(anio, mesInicio + 1, 1);
	}
	
	public LocalDate obtieneFechaFin(String cuatrimestre, Integer anio) 
	{
        int mesFin = -1;
		
		if(cuatrimestre.equalsIgnoreCase("PRIMER CUATRIMESTRE"))
			mesFin = 3;
		else if(cuatrimestre.equalsIgnoreCase("SEGUNDO CUATRIMESTRE")) 
			mesFin = 7;
		else if(cuatrimestre.equalsIgnoreCase("TERCER CUATRIMESTRE")) 
			mesFin = 11;
		else
			throw new BadRequestException("Descripcion de cuatrimestre invalido");

		return YearMonth.of(anio, mesFin + 1).atEndOfMonth();
	}
	
	public List<BinnacleInsResponse> buscaBitacorasXCuatrimestre(String cuatrimestre, Integer anio) 
	{
		if(cuatrimestre == null)
			throw new BadRequestException("Periodo invalido");
		
		if(anio == null || (anio.toString().length() != 4))
			throw new BadRequestException("Año invalido");
		
		LocalDate  fechaIni = obtieneFechaIni(cuatrimestre, anio);
		LocalDate  fechaFin = obtieneFechaFin(cuatrimestre, anio);

		List<BinnacleIns> lst = binnacleInsServ.buscaBitacorasXCuatrimestre(fechaIni, fechaFin);
		
		if(lst.isEmpty())
			throw new RecursoNoEncontradoException("No se encontro informacion con el periodo seleccionado");
		
		List<BinnacleInsResponse> lstResp = new ArrayList<>();
		
		lst.stream().forEach(e -> 
		{
			lstResp.add(toBinnacleInsResponse(e));
		});
		
		return lstResp;
	}
	
	public BinnacleInsResponse toBinnacleInsResponse(BinnacleIns vo)
	{
		BinnacleInsResponse voR = new BinnacleInsResponse();
		
		voR.setId(vo.getId());
		voR.setDriverCode(vo.getDriverCode());
		voR.setStatusGP(vo.getStatusGP());
		voR.setDateCalculation(vo.getDateCalculation());
		voR.setDateStart(vo.getDateStart());
		voR.setDateEnd(vo.getDateEnd());
		voR.setKilometers(vo.getKilometers());
		voR.setRosterStatus(vo.getRosterStatus());
		voR.setLstInsDetailsResp(toListInsDetailResponse(vo.getLstInsDetails()));
		return voR;
	}
	
	public List<InsDetailResponse> toListInsDetailResponse(List<InsDetail> lst)
	{
		if(lst == null)
			return new ArrayList<>();
		
		return lst.stream().map(e -> toInsDetailResponse(e)).toList();
	}
	
	public InsDetailResponse toInsDetailResponse(InsDetail vo) 
	{
		InsDetailResponse voR = new InsDetailResponse();
		
		voR.setId(vo.getId());
		voR.setDegreeCode(vo.getDegreeCode());
		voR.setDegreeDescription(vo.getDegreeDescription());
		voR.setSanctionCode(vo.getSanctionCode());
		voR.setSanctiondescription(vo.getSanctiondescription());
		voR.setDriverCode(vo.getDriverCode());
		voR.setDateIncident(vo.getDateIncident());
		voR.setHourIncident(vo.getHourIncident());
		voR.setDegreeAffects(vo.getDegreeAffects());
		voR.setStsSegopDegree(vo.getStsSegopDegree());
		voR.setDateTravelGrd(vo.getDateTravelGrd());
		voR.setTravelIdGrd(vo.getTravelIdGrd());
		voR.setResponsibility(vo.getResponsibility());
		voR.setStsSegopResp(vo.getStsSegopResp());
		voR.setDateTravelRes(vo.getDateTravelRes());
		voR.setTravelIdRes(vo.getTravelIdRes());
		voR.setDateSanctioned(vo.getDateSanctioned());
		
		return voR;
	}
}
