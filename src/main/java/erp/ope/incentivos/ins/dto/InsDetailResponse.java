package erp.ope.incentivos.ins.dto;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import erp.ope.incentivos.ins.model.InsDetail;
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
	
	public InsDetailResponse(InsDetail vo) 
	{
		this.id = vo.getId();
		this.degreeCode = vo.getDegreeCode();
		this.degreeDescription = vo.getDegreeDescription();
		this.sanctionCode = vo.getSanctionCode();
		this.sanctiondescription = vo.getSanctiondescription();
		this.driverCode = vo.getDriverCode();
		this.dateIncident = vo.getDateIncident();
		this.hourIncident = vo.getHourIncident();
		this.degreeAffects = vo.getDegreeAffects();
		this.stsSegopDegree = vo.getStsSegopDegree();
		this.dateTravelGrd = vo.getDateTravelGrd();
		this.travelIdGrd = vo.getTravelIdGrd();
		this.responsibility = vo.getResponsibility();
		this.stsSegopResp = vo.getStsSegopResp();
		this.dateTravelRes = vo.getDateTravelRes();
		this.travelIdRes = vo.getTravelIdRes();
		this.dateSanctioned = vo.getDateSanctioned();
	}
}
