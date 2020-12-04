package de.immomio.model.repository.core.landlord.product.customer.addon;

import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.model.abstractrepository.product.BaseAbstractCustomerAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "customerAddonProducts")
public interface BaseCustomerAddonProductRepository
        extends BaseAbstractCustomerAddonProductRepository<LandlordCustomerAddonProduct> {

}
