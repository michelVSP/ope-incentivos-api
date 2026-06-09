package erp.ope.incentivos.ins.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class KmsGoalINSpk 
{
	@Column(name = "REGION_CODE")
	private String plazaCobro;
	
	@Column(name = "BRAND_CODE")
	private String brandCode;
	
	@Column(name = "ZONE_CODE")
	private String zoneCode;

	@Column(name = "YEAR_PERIOD")
	private Integer yearPeriod;
	
	@Column(name = "PERIOD")
	private Integer period;
}
