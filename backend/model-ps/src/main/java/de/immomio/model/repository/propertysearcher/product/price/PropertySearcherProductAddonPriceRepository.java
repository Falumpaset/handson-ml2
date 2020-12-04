package de.immomio.model.repository.propertysearcher.product.price;

import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductAddonPrice;
import de.immomio.model.abstractrepository.product.price.AbstractProductAddonPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "productAddonPrices")
public interface PropertySearcherProductAddonPriceRepository
        extends AbstractProductAddonPriceRepository<PropertySearcherProductAddonPrice> {

}
