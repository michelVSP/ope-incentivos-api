package erp.ope.incentivos.ins.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DEGREE_INCIDENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanctionsIncidents 
{

	@Id
	@Column(name = "DEGREE_CODE")
	private String sanctionCode;
	
	@Column(name = "DEGREE_DESCRIPTION")
	private String sanctionDescription;
	
	@Column(name = "AFFECTATION_INS")
	private Integer affectationINS;
		
}
