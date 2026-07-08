package erp.ope.incentivos.bpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.bpa.model.BpaDetail;

@Repository
public interface BpaDetailRepository extends JpaRepository<BpaDetail, Integer>
{
	@Query("""
			select a
			from BpaDetail a
			where a.yearCalculated = :yearCalculated
			and a.driverCode in :codes
			""")
	List<BpaDetail> findByYearCalculatedAndDriverCodeIn(Integer yearCalculated, List<String> codes);
}
