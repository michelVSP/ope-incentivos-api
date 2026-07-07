package erp.ope.incentivos.bpa.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class BpaParamPK
{
	@Column(name = "incentive_Code")
	private String incentiveCode;
	
	@Column(name = "param_Name")
	private String paramName;
}
