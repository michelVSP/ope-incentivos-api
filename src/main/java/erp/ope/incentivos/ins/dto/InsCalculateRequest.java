package erp.ope.incentivos.ins.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InsCalculateRequest 
{
	@NotNull
	@Min(2020)
	private Integer year;
	
	@NotNull
	@Pattern(regexp = "PRIMER CUATRIMESTRE|SEGUNDO CUATRIMESTRE|TERCER CUATRIMESTRE")
	private String period;
}
