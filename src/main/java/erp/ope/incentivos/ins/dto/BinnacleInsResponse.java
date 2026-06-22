package erp.ope.incentivos.ins.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinnacleInsResponse 
{
	private Integer id;
	
	private String driverCode;
	
	private Integer statusGP;
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate dateCalculation;
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate dateStart;
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate dateEnd;
	
	private Integer kilometers;
	
	private Double incentiveAmount;
	
	private Integer rosterStatus;
	
	private List<InsDetailResponse> lstInsDetailsResp = new ArrayList<>();
}
