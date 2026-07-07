package erp.ope.incentivos.bpa.controllers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import erp.ope.incentivos.bpa.dto.DriverResponse;
import erp.ope.incentivos.bpa.services.BpaCalculateAnualService;
import erp.ope.incentivos.bpa.services.BpaFindCalculateService;
import erp.ope.incentivos.exception.BadRequestException;
import erp.ope.incentivos.exception.RecursoNoEncontradoException;

@RestController
@RequestMapping(path = "/api/v1/bpa")
public class BpaCalculateController 
{
	BpaFindCalculateService serviceFind;
	BpaCalculateAnualService serviceCalculate;
	
	public BpaCalculateController(BpaFindCalculateService serviceFind, BpaCalculateAnualService serviceCalculate)
	{
		this.serviceFind = serviceFind;
		this.serviceCalculate = serviceCalculate;
	}
	
	@GetMapping("/eligible-drivers")
	public ResponseEntity<List<DriverResponse>> findElegibleDrivers(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCalculate)
	{
		if(dateCalculate == null)
			throw new BadRequestException("No se dio una fecha para calcular ");
		
		List<DriverResponse> lst = serviceFind.findDriversAniversary(dateCalculate);
		return ResponseEntity.ok(lst);
	}
	
	@PostMapping("/calculate-bpa")
	public void calculateAnualBpa(@RequestBody List<String> lstDrivers,
								  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCalculate)
	{
		if(dateCalculate == null)
			throw new BadRequestException("No se dio una fecha para calcular ");
		
		if(lstDrivers == null || lstDrivers.isEmpty())
			throw new RecursoNoEncontradoException("No se dieron claves de conductores");
		
		serviceCalculate.calculateBPAOnAbsoluteDate(lstDrivers, dateCalculate);
	}
	
	@PostMapping("/calculate-bpa-absolute")
	public void calculateAnualBpaOnAbsoluteDate( @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateCalculate)
	{
		if(dateCalculate == null)
			throw new BadRequestException("No se dio una fecha para calcular ");
		
		Collection<DriverResponse> lst = serviceFind.findDriversAniversary(dateCalculate);
		if(lst == null || lst.isEmpty())
			throw new RecursoNoEncontradoException("No se encontraron conductores con la fecha de aniversario proporcionada");
		
//		Map<String, DriverRequest> map = serviceFind.convertDriverResponseToRequest(lst);
//		
//		serviceCalculate.calculateBPAOnAbsoluteDate(map, dateCalculate);
	}
}
