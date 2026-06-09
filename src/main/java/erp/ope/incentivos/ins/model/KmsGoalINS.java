package erp.ope.incentivos.ins.model;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ins_kms_goals")
@Data
public class KmsGoalINS 
{
	@EmbeddedId
	private KmsGoalINSpk pk;
	
	@Column(name = "START_PERIOD")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate startPeriod;
	
	@Column(name = "END_PERIOD")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate endPeriod;
	
	@Column(name = "KMS")
	private Integer kms;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "LAST_UPDATE_TIME")
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	private LocalDate lastUpdateTime;
	
	@Column(name = "LAST_UPDATE_USER")
	private String lastUpdateUser;

}
