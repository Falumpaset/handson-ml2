package de.immomio.model.repository.service.landlord.product.customer;

import de.immomio.model.repository.core.landlord.product.customer.BaseCustomerProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-customerProducts")
public interface LandlordCustomerProductRepository extends BaseCustomerProductRepository {

}
