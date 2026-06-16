package erp.ope.incentivos.ins.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import erp.ope.incentivos.ins.model.BinnacleIns;
import erp.ope.incentivos.ins.model.InsDetail;
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
	
	public BinnacleInsResponse(BinnacleIns vo)
	{
		this.id = vo.getId();
		this.driverCode = vo.getDriverCode();
		this.statusGP = vo.getStatusGP();
		this.dateCalculation = vo.getDateCalculation();
		this.dateStart = vo.getDateStart();
		this.dateEnd = vo.getDateEnd();
		this.kilometers = vo.getKilometers();
		this.rosterStatus = vo.getRosterStatus();
		
		setLstDetails(vo.getLstInsDetails());
	}
	
	public void setLstDetails(List<InsDetail> lst)
	{
		if(lst == null)
			return;
		
		lst.stream().forEach(e -> { addDetail(e); });
	}
	
	public void addDetail(InsDetail detail) 
	{
	    this.lstInsDetailsResp.add(new InsDetailResponse(detail));
	}
}
