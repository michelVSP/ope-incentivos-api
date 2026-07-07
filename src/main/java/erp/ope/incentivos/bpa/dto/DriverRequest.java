package erp.ope.incentivos.bpa.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest 
{
	String driverCode;
	String firstName;
	String paternalLastName;
	String maternalLastName;
	
	Integer	yearsOfService;
	
	boolean alreadyCalculated = false;
	
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate regionEntryDate;
	
}
