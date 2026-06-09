package erp.ope.incentivos.ins.model;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRAVEL_INCIDENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelIncidents 
{	
	@EmbeddedId
	private TravelIncidentsPK pk;
	
	@Column(name = "REGISTRATION_DATE")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp registrationDate;
	
	@Column(name = "DATE_INCIDENT")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateIncident;

	@Column(name = "HOUR_INCIDENT")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp hourIncident;

	@Column(name = "DRIVER_CODE")
	private String driverCode;
	
	@Column(name = "DEGREE_CODE")
	private String degreeCode;
	
	@Column(name = "DEGREE_DESCRIPTION")
	private String degreeDescription;
	
	@Column(name = "SANCTION_CODE")
	private String sanctionCode;
	
	@Column(name = "SANCTION_DESCRIPTION")
	private String sanctionDescription;
	
	@Column(name = "DATE_SANCTIONED")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp dateSanctioned;

	@Column(name = "TRAVEL_SOURCE_SYSTEM")
	private String travelSourceSystem;
	
	@Column(name = "TRAVEL_SOURCE_ID")
	private String travelSourceId;
	
	@Column(name = "LAST_UPDATE_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp lastUpdateTime;

	@Column(name = "LOCAL_LAST_UPDATE_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private Timestamp localLastUpdateTime;
	
}
