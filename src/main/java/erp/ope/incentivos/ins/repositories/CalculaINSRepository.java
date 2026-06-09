package erp.ope.incentivos.ins.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.ins.model.KmsGoalINS;
import erp.ope.incentivos.ins.model.KmsGoalINSpk;

@Repository
public interface CalculaINSRepository extends JpaRepository<KmsGoalINS, KmsGoalINSpk>
{
	
}
