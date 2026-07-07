package erp.ope.incentivos.bpa.model;

import erp.ope.incentivos.bpa.model.pk.BpaParamPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bpa_Param")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpaParam 
{
	@EmbeddedId
	BpaParamPK pk;
	
	@Column(name = "param_Value")
	String paramValue;
	
	@Column(name = "param_Status")
	Integer paramStatus;
	
	@Column(name = "incentive_Description")
	String incentiveDescription;
}
