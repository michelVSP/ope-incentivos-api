package erp.ope.incentivos.ins.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.ins.model.DegreeIncidents;

@Repository
public interface DegreeIncidentsRepository extends JpaRepository<DegreeIncidents, String>
{

}
