package de.immomio.model.repository.landlord.product.customer.addon;

import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.model.abstractrepository.product.customer.AbstractCustomerAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "customerAddonProducts")
public interface LandlordCustomerAddonProductRepository
        extends AbstractCustomerAddonProductRepository<LandlordCustomerAddonProduct>,
        LandlordCustomerAddonProductRepositoryCustom {

}
