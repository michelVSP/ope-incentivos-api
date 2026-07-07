package erp.ope.incentivos.bpa.model;

import erp.ope.incentivos.bpa.model.pk.DriverIncentivePK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Driver_Incentive")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverIncentive 
{
	@EmbeddedId
	DriverIncentivePK pk;
	
	@Column(name = "frecuency")
	String frecuency;
	
	@Column(name = "incentive_Description")
	String incentiveDescription;
	
	@Column(name = "status_Incentive")
	Integer statusIncentive;
	
	@Column(name = "amount")
	Integer amount;
}
