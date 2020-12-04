package de.immomio.model.repository.core.shared.contract;

import de.immomio.data.landlord.entity.contract.DigitalContractApiUser;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * @author Andreas Hansen
 */

@RepositoryRestResource(exported = false)
public interface BaseDigitalContractApiUserRepository extends JpaRepository<DigitalContractApiUser, Long> {

    Optional<DigitalContractApiUser> findByCustomer(LandlordCustomer customer);

    boolean existsByCustomer(LandlordCustomer customer);

}
