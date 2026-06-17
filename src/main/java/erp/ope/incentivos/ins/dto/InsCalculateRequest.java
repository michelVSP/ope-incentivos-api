package erp.ope.incentivos.ins.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsCalculateRequest 
{
	int year;
	String period;
	int totalDrivers = 0;
	int processed = 0;
	int skipped = 0;
	int eligible = 0;
	int ineligible = 0;
}
