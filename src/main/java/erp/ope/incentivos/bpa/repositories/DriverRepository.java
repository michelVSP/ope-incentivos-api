package erp.ope.incentivos.bpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.bpa.model.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, String>
{
	List<Driver> findAllByHrStatus(Integer hrStatus);
	
	@Query("""
			select d from Driver d where d.driverCode in (:codes) and d.hrStatus = 1
			""")
	List<Driver> findByDriverCodesIn(String codes);
}
