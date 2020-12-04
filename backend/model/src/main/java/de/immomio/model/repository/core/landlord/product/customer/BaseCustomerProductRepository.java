package de.immomio.model.repository.core.landlord.product.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.model.abstractrepository.product.BaseAbstractCustomerProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Kingma
 */

@RepositoryRestResource(path = "customerProducts")
public interface BaseCustomerProductRepository extends BaseAbstractCustomerProductRepository<LandlordCustomerProduct,
        LandlordCustomer> {

}
