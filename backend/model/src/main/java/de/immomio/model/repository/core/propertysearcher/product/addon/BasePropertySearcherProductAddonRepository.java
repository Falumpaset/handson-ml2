package de.immomio.model.repository.core.propertysearcher.product.addon;

import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.data.propertysearcher.entity.product.addon.PropertySearcherAddonProduct;
import de.immomio.data.propertysearcher.entity.product.addon.PropertySearcherProductAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ps-productAddons")
public interface BasePropertySearcherProductAddonRepository extends JpaRepository<PropertySearcherProductAddon, Long> {

    PropertySearcherProductAddon findByProductAndAddonProduct(PropertySearcherProduct product,
                                                              PropertySearcherAddonProduct addonProduct);

}
