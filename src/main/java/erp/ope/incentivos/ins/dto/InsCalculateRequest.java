package erp.ope.incentivos.ins.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsCalculateRequest 
{
	@NotNull
	@Min(2020)
	private Integer year;
	
	@NotNull
	private String period;
}
