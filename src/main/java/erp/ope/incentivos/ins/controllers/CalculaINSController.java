package erp.ope.incentivos.ins.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import erp.ope.incentivos.ins.dto.BinnacleInsResponse;
import erp.ope.incentivos.ins.dto.InsCalculateResponse;
import erp.ope.incentivos.ins.services.CalculaINSService;

@RestController
@RequestMapping(path = "/api/v1/ins")
public class CalculaINSController 
{
	private final CalculaINSService service;
	
	public CalculaINSController(CalculaINSService service) 
	{
		this.service = service;
	}
	
	@PostMapping("/calculaIns/{cuatrimestre}/{anio}")
	public ResponseEntity<InsCalculateResponse> procesaCalculoINS(
	        @PathVariable String cuatrimestre,
	        @PathVariable Integer anio) throws Exception
	{
		InsCalculateResponse vo = service.procesaCalculoINS(cuatrimestre, anio);
	    return ResponseEntity.ok(vo);
	}
	
	@GetMapping("/buscaCalculo/{cuatrimestre}/{anio}")
	public ResponseEntity<List<BinnacleInsResponse>> buscaBitacorasXCuatrimestre(
	        @PathVariable String cuatrimestre,
	        @PathVariable Integer anio) throws Exception 
	{
	    List<BinnacleInsResponse> lst = service.buscaBitacorasXCuatrimestre(cuatrimestre, anio);
	    
	    return ResponseEntity.ok(lst);
	}
}
