package erp.ope.incentivos.bpa.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.bpa.model.DriverEvent;
import erp.ope.incentivos.bpa.model.pk.DriverEventPK;

@Repository
public interface DriverEventRepository extends JpaRepository<DriverEvent, DriverEventPK>
{
	@Query("""
			select e 
			from DriverEvent e
			where e.driverCode in (:driverCodes)
			and e.eventCode in (:eventCodes)
			and e.startTime between :fecStart and :fecEnd
			""")
	List<DriverEvent> findByEventCodeAndDriverCodeAndStartTimeBetween(String driverCodes, String eventCodes, LocalDate fecStart, LocalDate fecEnd);
}
