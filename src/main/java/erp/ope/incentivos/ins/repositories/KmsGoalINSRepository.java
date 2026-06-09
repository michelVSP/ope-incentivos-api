package erp.ope.incentivos.ins.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.ins.model.KmsGoalINS;
import erp.ope.incentivos.ins.model.KmsGoalINSpk;

@Repository
public interface KmsGoalINSRepository extends JpaRepository<KmsGoalINS, KmsGoalINSpk>
{
	@Query("""
			select k from KmsGoalINS k where k.pk.yearPeriod = :anio and k.pk.period = :period
			""")
	List<KmsGoalINS> findByYearAndPeriodo(Integer anio, Integer period);
}
