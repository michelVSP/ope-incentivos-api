package erp.ope.incentivos.bpa.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse 
{
	@JsonFormat( shape = JsonFormat.Shape.STRING, pattern =  "yyyy-MM-dd", timezone = "America/Mexico_City")
	LocalDate dateProcess;
	
	Integer totalRequested = 0;
	Integer processed = 0;
	Integer skipped = 0;
	Integer gained = 0;
	Integer lost = 0;
	
	List<String> skippedDriverCodes = new ArrayList<>();
	List<DriverCalculateResponse> results = new ArrayList<>();
}
