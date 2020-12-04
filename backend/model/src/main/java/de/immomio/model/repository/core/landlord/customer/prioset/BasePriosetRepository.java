package de.immomio.model.repository.core.landlord.customer.prioset;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "priosets")
public interface BasePriosetRepository extends JpaRepository<Prioset, Long> {

    @RestResource(exported = false)
    Prioset findFirstByCustomerOrderByCreatedDesc(LandlordCustomer customer);
}
