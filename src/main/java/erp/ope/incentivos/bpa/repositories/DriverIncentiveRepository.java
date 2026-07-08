package erp.ope.incentivos.bpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import erp.ope.incentivos.bpa.model.DriverIncentive;
import erp.ope.incentivos.bpa.model.pk.DriverIncentivePK;

@Repository
public interface DriverIncentiveRepository extends JpaRepository<DriverIncentive, DriverIncentivePK> 
{

}
