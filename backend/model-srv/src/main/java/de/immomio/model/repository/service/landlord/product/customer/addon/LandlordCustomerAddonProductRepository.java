package de.immomio.model.repository.service.landlord.product.customer.addon;

import de.immomio.model.repository.core.landlord.product.customer.addon.BaseCustomerAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-customerAddonProducts")
public interface LandlordCustomerAddonProductRepository extends BaseCustomerAddonProductRepository {

}
