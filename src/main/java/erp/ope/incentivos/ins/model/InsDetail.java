package erp.ope.incentivos.ins.model;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "INS_DETAIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsDetail 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INS_DETAIL_id")
	private Integer id;	
	
	@jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
	@jakarta.persistence.JoinColumn(name = "binnacle_id", nullable = false)
	@com.fasterxml.jackson.annotation.JsonIgnore // Evita ciclos infinitos al serializar a JSON
	private BinnacleIns binnacleIns;
	
	@Column(name = "DEGREE_CODE")
	private String degreeCode;
	
	@Column(name = "DEGREE_DESCRIPTION")
	private String degreeDescription;
	
	@Column(name = "SANCTION_CODE")
	private String sanctionCode;
	
	@Column(name = "SANCTION_DESCRIPTION")
	private String sanctiondescription;

	@Column(name = "DRIVER_CODE")
	private String driverCode;

	@Column(name = "DATE_INCIDENT")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateIncident;
	
	@Column(name = "HOUR_INCIDENT")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp hourIncident;
	
	@Column(name = "Degree_Affects")
	private Integer degreeAffects;	
	
	@Column(name = "sts_segop_degree")
	private Integer stsSegopDegree;	
	
	@Column(name = "travel_date_grd")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateTravelGrd;
	
	@Column(name = "travel_id_grd")
	private Integer travelIdGrd;
	
	@Column(name = "responsibility")
	private Integer responsibility;	
	
	@Column(name = "sts_segop_resp")
	private Integer stsSegopResp;	
	
	@Column(name = "travel_date_res")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateTravelRes;
	
	@Column(name = "travel_id_res")
	private Integer travelIdRes;

	@Column(name = "DATE_SANCTIONED")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateSanctioned;
	
}
