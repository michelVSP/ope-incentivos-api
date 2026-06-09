package erp.ope.incentivos.ins.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import erp.ope.incentivos.ins.model.InsDetail;

@Repository
public interface InsDetailRespository extends JpaRepository<InsDetail, Integer>
{
	@Modifying
	@Transactional
	@Query("""
			DELETE FROM InsDetail idd where idd.binnacleIns.id in ( Select distinct(a.id)
																from BinnacleIns a 
																where a.dateStart between :fechaIni and :fechaFin )
			""")
	int deleteByDateStartBetween(LocalDate fechaIni, LocalDate fechaFin);

}
