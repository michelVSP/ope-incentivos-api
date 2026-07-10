package erp.ope.incentivos.bpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverCalculateResponse 
{
	String driverCode;
	Integer totalDays;
	Integer minDaysRequired;
	Integer bonusDays;
	Integer	statusGl;
}
