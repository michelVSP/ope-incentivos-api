package erp.ope.incentivos.bpa.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class DriverEventPK 
{
	@Column(name = "source_System")
	private String sourceSystem;
	
	@Column(name = "source_Id")
	private String sourceId;
}
