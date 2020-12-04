package de.immomio.model.repository.service.propertysearcher.product.customer.addon;

import de.immomio.model.repository.core.propertysearcher.product.customer.addon.BasePropertySearcherCustomerAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-customerAddonProducts")
public interface PropertySearcherCustomerAddonProductRepository
        extends BasePropertySearcherCustomerAddonProductRepository {

}
