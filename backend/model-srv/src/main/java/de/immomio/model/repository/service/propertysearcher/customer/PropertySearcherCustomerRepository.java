package de.immomio.model.repository.service.propertysearcher.customer;

import de.immomio.model.repository.core.propertysearcher.customer.BasePropertySearcherCustomerRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ps-customers")
public interface PropertySearcherCustomerRepository extends BasePropertySearcherCustomerRepository {

}
