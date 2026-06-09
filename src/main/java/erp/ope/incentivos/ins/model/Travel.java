package erp.ope.incentivos.ins.model;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRAVELS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Travel 
{	
	@EmbeddedId
	private TravelPK pk;
	
	@Column(name = "ORIGIN_CODE")
	private String originCode;
	
	@Column(name = "DEPARTURE_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate departureTime;
	
	@Column(name = "ARRIVAL_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate arrivalTime;
	
	@Column(name = "DESTINATION_CODE")
	private String destinationCode;
	
	@Column(name = "BUS_CODE")
	private String busCode;
	
	@Column(name = "DRIVER1_CODE")
	private String driver1Code;
	
	@Column(name = "DRIVER1_NAME")
	private String driver1Name;
	
	@Column(name = "DRIVER2_CODE")
	private String driver2Code;
	
	@Column(name = "DRIVER2_NAME")
	private String driver2Name;
	
	@Column(name = "KMS")
	private Integer kms;
	
	@Column(name = "TRIP_STATUS")
	private String tripStatus;
	
	@Column(name = "SALESTRIP_STATUS")
	private String salestripStatus;
	
	@Column(name = "LAST_UPDATE_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate lastUpdateTime;
	
	@Column(name = "LOCAL_LAST_UPDATE_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate localLastUpdateTime;
	
}
