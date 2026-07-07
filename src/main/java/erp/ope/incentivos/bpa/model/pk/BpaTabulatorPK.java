package erp.ope.incentivos.bpa.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class BpaTabulatorPK 
{
	@Column(name = "minimum_Seniority")
	private Integer minimumSeniority;
	
	@Column(name = "maximum_Seniority")
	private Integer maximumSeniority;
}
