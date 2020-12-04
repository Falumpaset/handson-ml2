package de.immomio.model.repository.propertysearcher.product.customer.addon;

import de.immomio.data.propertysearcher.entity.customer.product.addon.PropertySearcherCustomerAddonProduct;
import de.immomio.model.abstractrepository.product.customer.AbstractCustomerAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "customerAddonProducts")
public interface PropertySearcherCustomerAddonProductRepository
        extends AbstractCustomerAddonProductRepository<PropertySearcherCustomerAddonProduct> {

}
