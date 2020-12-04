package de.immomio.model.repository.service.propertysearcher.product.price;

import de.immomio.model.repository.core.propertysearcher.product.price.BasePropertySearcherProductAddonPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-productAddonPrices")
public interface PropertySearcherProductAddonPriceRepository extends BasePropertySearcherProductAddonPriceRepository {

}
