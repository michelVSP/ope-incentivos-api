package erp.ope.incentivos.ins.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import erp.ope.incentivos.ins.model.BinnacleIns;

@Repository
public interface BinnacleInsRepository extends JpaRepository<BinnacleIns, Integer>
{
	
	@Query("""
		Select a 
		from BinnacleIns a 
		where a.rosterStatus = :rosterStatus
		and a.dateStart between :fechaIni and :fechaFin
		""")
	List<BinnacleIns> findByRosterStatusAndDateStartBetween(Integer rosterStatus, LocalDate fechaIni, LocalDate fechaFin);
	
	@Modifying
	@Transactional
	@Query(""" 
			Delete from BinnacleIns where dateStart between :fechaIni and :fechaFin
			""")	
	int deleteByDateStartBetween(LocalDate fechaIni, LocalDate fechaFin);
	
	@Query("""
			Select a 
			from BinnacleIns a 
			where a.dateStart between :fechaIni and :fechaFin
			""")
	List<BinnacleIns> findByDateStartBetween(LocalDate fechaIni, LocalDate fechaFin);
}