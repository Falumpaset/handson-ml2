package de.immomio.model.repository.landlord.product.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.model.abstractrepository.product.customer.AbstractCustomerProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "customerProducts")
public interface LandlordCustomerProductRepository extends AbstractCustomerProductRepository<LandlordCustomerProduct,
        LandlordCustomer>
        , LandlordCustomerProductRepositoryCustom {

}
