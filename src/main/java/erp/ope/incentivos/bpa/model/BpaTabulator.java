package erp.ope.incentivos.bpa.model;

import erp.ope.incentivos.bpa.model.pk.BpaTabulatorPK;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bpa_Tabulator")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BpaTabulator 
{
	@EmbeddedId
	BpaTabulatorPK pk;
	
	@Column(name =  "bonus_Days")
	Integer bonus_Days;
}
