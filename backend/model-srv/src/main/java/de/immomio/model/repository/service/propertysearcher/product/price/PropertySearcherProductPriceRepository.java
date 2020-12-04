package de.immomio.model.repository.service.propertysearcher.product.price;

import de.immomio.model.repository.core.propertysearcher.product.price.BasePropertySearcherProductPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-productPrices")
public interface PropertySearcherProductPriceRepository extends BasePropertySearcherProductPriceRepository {

}
