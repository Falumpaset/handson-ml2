package de.immomio.model.repository.service.propertysearcher.product.customer;

import de.immomio.model.repository.core.propertysearcher.product.customer.BasePropertySearcherCustomerProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-customerProducts")
public interface PropertySearcherCustomerProductRepository extends BasePropertySearcherCustomerProductRepository {

}
