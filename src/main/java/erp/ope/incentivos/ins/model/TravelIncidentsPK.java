package erp.ope.incentivos.ins.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class TravelIncidentsPK 
{
	@Column(name = "SOURCE_SYSTEM")
	private String sourceSystem;
	
	@Column(name = "SOURCE_ID")
	private String sourceId;
}
