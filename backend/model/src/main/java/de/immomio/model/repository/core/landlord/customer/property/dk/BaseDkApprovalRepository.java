package de.immomio.model.repository.core.landlord.customer.property.dk;

import de.immomio.data.landlord.entity.property.dk.DkApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface BaseDkApprovalRepository extends JpaRepository<DkApproval, Long> {

    @RestResource(exported = false)
    List<DkApproval> findByApplicationId(Long applicationId);

}
