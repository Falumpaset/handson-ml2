package de.immomio.model.repository.service.landlord.customer;

import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-customers")
public interface LandlordCustomerRepository extends BaseLandlordCustomerRepository {

}
