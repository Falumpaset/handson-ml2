package de.immomio.model.repository.propertysearcher.product.price;

import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductPrice;
import de.immomio.model.abstractrepository.product.price.AbstractProductPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "productPrices")
public interface PropertySearcherProductPriceRepository
        extends AbstractProductPriceRepository<PropertySearcherProductPrice> {

}
