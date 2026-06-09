package erp.ope.incentivos.ins.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.ins.model.SanctionsIncidents;

@Repository
public interface SanctionsIncidentsRepository extends JpaRepository<SanctionsIncidents, String>
{

}
