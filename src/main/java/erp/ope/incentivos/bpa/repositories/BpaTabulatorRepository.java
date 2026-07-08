package erp.ope.incentivos.bpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.bpa.model.BpaTabulator;
import erp.ope.incentivos.bpa.model.pk.BpaTabulatorPK;

@Repository
public interface BpaTabulatorRepository extends JpaRepository<BpaTabulator, BpaTabulatorPK> 
{
	
}
