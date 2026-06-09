package erp.ope.incentivos.ins.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.ins.model.TravelIncidents;
import erp.ope.incentivos.ins.model.TravelIncidentsPK;

@Repository
public interface TravelIncidentaRepository extends JpaRepository<TravelIncidents, TravelIncidentsPK>
{
	@Query(""" 
			select t 
			from TravelIncidents t 
			where t.dateIncident 
			between :fechaIni and :fechaFin
			""")
    List<TravelIncidents> findByDateIncident(Timestamp fechaIni, Timestamp fechaFin);

}
