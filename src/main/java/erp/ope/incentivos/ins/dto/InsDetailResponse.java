package erp.ope.incentivos.ins.dto;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsDetailResponse 
{
	private Integer id;	
	
	private String degreeCode;
	
	private String degreeDescription;
	
	private String sanctionCode;
	
	private String sanctiondescription;

	private String driverCode;

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateIncident;
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp hourIncident;
	
	private Integer degreeAffects;	
	
	private Integer stsSegopDegree;	
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateTravelGrd;
	
	private Integer travelIdGrd;
	
	private Integer responsibility;	
	
	private Integer stsSegopResp;	
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateTravelRes;
	
	private Integer travelIdRes;

	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateSanctioned;
	
}
