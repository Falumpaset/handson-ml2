package de.immomio.model.repository.service.landlord.customer.prioset;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.model.repository.core.landlord.customer.prioset.BasePriosetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "ll-priosets")
public interface PriosetRepository extends BasePriosetRepository {

    @RestResource(exported = false)
    Prioset findFirstByCustomerOrderByCreatedDesc(LandlordCustomer customer);

    Page<Prioset> findByCustomer(@Param("customer") LandlordCustomer customer, Pageable pageable);

}
