package de.immomio.model.repository.propertysearcher.product.addon;

import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.data.propertysearcher.entity.product.addon.PropertySearcherAddonProduct;
import de.immomio.data.propertysearcher.entity.product.addon.PropertySearcherProductAddon;
import de.immomio.model.abstractrepository.product.AbstractProductAddonRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "productAddons")
public interface PropertySearcherProductAddonRepository extends AbstractProductAddonRepository<
        PropertySearcherProductAddon, PropertySearcherProduct, PropertySearcherAddonProduct> {

}
