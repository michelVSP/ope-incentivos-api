package erp.ope.incentivos.ins.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
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
import erp.ope.incentivos.ins.model.BinnacleIns;
import erp.ope.incentivos.ins.model.DegreeIncidents;
import erp.ope.incentivos.ins.model.InsDetail;
import erp.ope.incentivos.ins.model.KmsGoalINS;
import erp.ope.incentivos.ins.model.SanctionsIncidents;
import erp.ope.incentivos.ins.repositories.BinnacleInsRepository;

@Service
public class CalculaINSService 
{
	private final DegreeIncidentsService degreeIncidServ;
	private final BinnacleInsService binnacleInsServ;
	private final InsDetailService insDetailService;
	private final SanctionsIncidentsService sanctionsIncidService;
	private final KmsGoalINSService kmsGoalINSService;
	private final TravelsService travelsService;
	private final BinnacleInsRepository binnacleInsRepository;
	
	public CalculaINSService(DegreeIncidentsService degreeIncidServ, 
								BinnacleInsService binnacleInsServ, 
								InsDetailService insDetailService,
								SanctionsIncidentsService sanctionsIncidService,
								KmsGoalINSService kmsGoalINSService,
								TravelsService travelsService,
								BinnacleInsRepository binnacleInsRepository) 
	{	
		this.degreeIncidServ = degreeIncidServ;
		this.binnacleInsServ = binnacleInsServ;
		this.insDetailService = insDetailService;
		this.sanctionsIncidService = sanctionsIncidService;
		this.kmsGoalINSService = kmsGoalINSService;
		this.travelsService = travelsService;
		this.binnacleInsRepository = binnacleInsRepository;
	}

	public void procesaCalculoINS(String cuatrimestre, Integer anio) throws Exception 
	{
		if(cuatrimestre == null)
			throw new BadRequestException("Periodo invalido");
		
		if(anio == null || (anio.toString().length() != 4))
			throw new BadRequestException("Año invalido");
		
		boolean continuar = validaCalculoExistente(cuatrimestre, anio);
		if(!continuar)
			throw new CalculoDuplicadoException("El proceso de pago ya ha sido ejecutado por nómina, no es posible hacer el cálculo nuevamente.");
		
		procesaCalculoInsCuatrimestral(cuatrimestre, anio);
	}

	@Transactional
	private void procesaCalculoInsCuatrimestral(String cuatrimestre, Integer anio) throws Exception 
	{
		Date f1 = obtieneFechaIni(cuatrimestre, anio);
		Date f2 = obtieneFechaFin(cuatrimestre, anio);
		
		LocalDate  fechaIni = f1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate  fechaFin = f2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		////  limpia Bitacoras anteriores no pagadas por nomina
		limpiaInsBitacora_InsDetalle(fechaIni, fechaFin);
		
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
			validaIncentivoINS(acumulados, mapDreegreInc, mapSanctions, kmsGOAL);
			bitacoras.putAll(acumulados);
		});
		
		/////  guarda resultado evaluacion de incentivo
		guardaEnInsBitacoraInsDetalle(bitacoras);
	}

	@Transactional
	private void guardaEnInsBitacoraInsDetalle(Map<String, BinnacleIns> acumulados) 
	{
		int count = 0;
		for (BinnacleIns vo : acumulados.values()) 
		{
			binnacleInsRepository.save(vo);
			if(count % 500 == 0)
			{
				binnacleInsRepository.flush();
				count = 0;
			}
			count++;
		}
	}

	private void validaIncentivoINS(Map<String, BinnacleIns> acumulados, Map<String, DegreeIncidents> mapDreegreInc,
									Map<String, SanctionsIncidents> mapSanctions, KmsGoalINS e) 
	{
		for (BinnacleIns bitacora : acumulados.values()) 
		{
			boolean estatusGP = validaAlcanzaIncentivoINS(bitacora, e.getKms(), mapDreegreInc, mapSanctions);
			bitacora.setStatusGP(estatusGP ? 1 : 0);
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
		
		tieneAfectacion = bitacora.getLstInsDetails().stream().map(x -> mapSanctions.get(x.getDegreeCode()))
													.filter(Objects::nonNull)
													.anyMatch(y -> Integer.valueOf(1).equals(y.getAffectationINS()));
		if(tieneAfectacion)
			return false;
		
		return true;
	}

	private void relacionaViajesAcomuladosVSIncidencia(Map<String, BinnacleIns> acumulados, Map<String, List<InsDetail>> mapINSDetail, LocalDate  fechaIni, LocalDate fechaFin) 
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

	private Map<String, BinnacleIns> consultaAcumuladoPorCuatrimestre(String regionId, String marcaId, String zonaId, LocalDate fechaIni, LocalDate fechaFin) 
	{
		List<BinnacleIns> lst = travelsService.consultaAcumuladoPorCuatrimestre(regionId, marcaId, zonaId, fechaIni, fechaFin);
		Map<String, BinnacleIns> map = lst.stream().collect(Collectors.toMap(BinnacleIns :: getDriverCode, e ->e));
		return map;
	}

	private void limpiaInsBitacora_InsDetalle(LocalDate fechaIni, LocalDate fechaFin) throws Exception 
	{
		insDetailService.limpiaInsBitacoraInsDetalle(fechaIni, fechaFin);
		binnacleInsServ.limpiaInsBitacoraInsDetalle(fechaIni, fechaFin);
	}

	private boolean validaCalculoExistente(String cuatrimestre, Integer anio) 
	{
		Date f1 = obtieneFechaIni(cuatrimestre, anio);
		Date f2 = obtieneFechaFin(cuatrimestre, anio);
		
		LocalDate  fechaIni = f1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate  fechaFin = f2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return binnacleInsServ.buscaEstatusNominaExistente(fechaIni, fechaFin);
	}
	
	private Date obtieneFechaIni(String cuatrimestre, Integer anio)
	{
		int mesInicio = 0;
		
		if(cuatrimestre.equalsIgnoreCase("PRIMER CUATRIMESTRE")) 
			mesInicio = 0;
		
		else if(cuatrimestre.equalsIgnoreCase("SEGUNDO CUATRIMESTRE")) 
			mesInicio = 4;
		
		else if(cuatrimestre.equalsIgnoreCase("TERCER CUATRIMESTRE")) 
			mesInicio = 8;
		
		if(mesInicio == 0)
			throw new BadRequestException("Descripcion de cuatrimestre invalido");
			
		Calendar calIni  = Calendar.getInstance();
		calIni.setTime(new java.util.Date());
        calIni.set(Calendar.DAY_OF_MONTH, 1);
        calIni.set(Calendar.MONTH, mesInicio);
        calIni.set(Calendar.YEAR, anio);
        calIni.set(Calendar.HOUR_OF_DAY, 0);
        calIni.set(Calendar.MINUTE, 0);
        calIni.set(Calendar.SECOND, 0);
        calIni.set(Calendar.MILLISECOND, 0);
        
        return new Date( calIni.getTime().getTime() );
	}
	
	private Date obtieneFechaFin(String cuatrimestre, Integer anio) 
	{
        int mesFin=0;
		
		if(cuatrimestre.equals("PRIMER CUATRIMESTRE"))
			mesFin = 3;
		else if(cuatrimestre.equals("SEGUNDO CUATRIMESTRE")) 
			mesFin = 7;
		else if(cuatrimestre.equals("TERCER CUATRIMESTRE")) 
			mesFin = 11;

        return getDateFinMes(anio, mesFin);
	}
	
	public Date getDateFinMes(int year, int mes)
	{
        Calendar cl = Calendar.getInstance();
        int dias = getMaxDiasEnMes(mes, year);
        cl.set(year, mes, dias, 23, 59, 00);
        return new Date(cl.getTime().getTime() ) ;
    }

	public int getMaxDiasEnMes(int mes, int year) 
	{
		switch (mes) 
		{
			case Calendar.JANUARY:
			case Calendar.MARCH:
			case Calendar.MAY:
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.OCTOBER:
			case Calendar.DECEMBER:
			default:
				return 31;

			case Calendar.APRIL:
			case Calendar.JUNE:
			case Calendar.SEPTEMBER:
			case Calendar.NOVEMBER:
				return 30;

			case Calendar.FEBRUARY: 
			{
				return getDiasFebrero(year);
			}
		}
	}
	
	private int getDiasFebrero(int year) 
    {
        Calendar cal = Calendar.getInstance();
        cal.set(year, 2, 0, 0, 0, 0);
        int myDia = 0;
        myDia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return myDia;
    }

	public List<BinnacleIns> buscaBitacorasXCuatrimestre(String cuatrimestre, Integer anio) 
	{
		if(cuatrimestre == null)
			throw new BadRequestException("Periodo invalido");
		
		if(anio == null || (anio.toString().length() != 4))
			throw new BadRequestException("Año invalido");
		
		Date f1 = obtieneFechaIni(cuatrimestre, anio);
		Date f2 = obtieneFechaFin(cuatrimestre, anio);
		
		LocalDate  fechaIni = f1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate  fechaFin = f2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		return binnacleInsServ.buscaBitacorasXCuatrimestre(fechaIni, fechaFin);
	}
	
}
