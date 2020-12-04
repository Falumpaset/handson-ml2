package de.immomio.model.repository.core.landlord.schufa;

import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Johannes Hiemer
 */
@RepositoryRestResource(path = "schufaJobs")
public interface BaseLandlordSchufaJobRepository extends JpaRepository<LandlordSchufaJob, Long> {

    List<LandlordSchufaJob> findByState(JobState state);

    List<LandlordSchufaJob> findByCustomer(LandlordCustomer landlordCustomer);

    List<LandlordSchufaJob> findByStateInAndAndType(List<JobState> states, CbiActionType actionType);

}
