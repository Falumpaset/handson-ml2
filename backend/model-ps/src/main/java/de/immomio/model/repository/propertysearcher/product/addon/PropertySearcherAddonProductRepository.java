package de.immomio.model.repository.propertysearcher.product.addon;

import de.immomio.data.propertysearcher.entity.product.addon.PropertySearcherAddonProduct;
import de.immomio.model.abstractrepository.product.addon.AbstractAddonProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "addonProducts")
public interface PropertySearcherAddonProductRepository extends
        AbstractAddonProductRepository<PropertySearcherAddonProduct> {
}
