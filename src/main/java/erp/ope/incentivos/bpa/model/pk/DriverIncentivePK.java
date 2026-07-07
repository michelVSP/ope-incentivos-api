package erp.ope.incentivos.bpa.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class DriverIncentivePK
{
	@Column(name = "driver_Code")
	private String driverCode;
	
	@Column(name = "month_Calculated")
	private Integer monthCalculated;
	
	@Column(name = "year_Calculated")
	private Integer yearCalculated;
	
	@Column(name = "incentive_Code")
	private String incentiveCode;
}
