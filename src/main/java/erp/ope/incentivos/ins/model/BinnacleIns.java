package erp.ope.incentivos.ins.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BINNACLE_INS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinnacleIns 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "binnacle_id")
	private Integer id;
	
	@Column(name = "DRIVER_CODE")
	private String driverCode;
	
	@Column(name = "status_gp")
	private Integer statusGP;
	
	@Column(name = "date_calculation")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate dateCalculation;
	
	@Column(name = "MONTH_YEAR_START")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate dateStart;
	
	@Column(name = "MONTH_YEAR_END")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate dateEnd;
	
	@Column(name = "kilometers")
	private Integer kilometers;
	
	@Column(name = "INCENTIVE_AMOUNT")
	private Double incentiveAmount;
	
	@Column(name = "roster_status")
	private Integer rosterStatus;
	
	@OneToMany(mappedBy = "binnacleIns", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InsDetail> lstInsDetails = new ArrayList<>();

	// AGREGA ESTE MÉTODO al final de la clase:
	public void addDetail(InsDetail detail) 
	{
	    if (this.lstInsDetails == null) 
	        this.lstInsDetails = new ArrayList<>();
	
	    this.lstInsDetails.add(detail);
	    detail.setBinnacleIns(this);
	}

	public void setLstDetails(List<InsDetail> lst)
	{
		lst.stream().forEach(e -> { addDetail(e); });
	}
}
