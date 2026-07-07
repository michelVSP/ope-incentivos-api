package erp.ope.incentivos.bpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import erp.ope.incentivos.bpa.model.BpaParam;
import erp.ope.incentivos.bpa.model.pk.BpaParamPK;

public interface BpaParamRepository extends JpaRepository<BpaParam, BpaParamPK>
{

}
