package erp.ope.incentivos.bpa.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import erp.ope.incentivos.bpa.model.pk.DriverEventPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEvent 
{
	@EmbeddedId
	DriverEventPK pk;
	
	@Column(name = "driver_Code")
	String driverCode;
	
	@Column(name = "event_Code")
	String eventCode;
	
	@Column(name = "event_desc")
	String eventDesc;
	
	@Column(name = "start_Time")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate startTime;
	
	@Column(name = "end_Time")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate endTime;
	
}
