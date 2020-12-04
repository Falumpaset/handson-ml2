package de.immomio.model.repository.service.propertysearcher.price;

import de.immomio.model.repository.core.propertysearcher.price.BasePropertySearcherPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-prices")
public interface PropertySearcherPriceRepository extends BasePropertySearcherPriceRepository {

}
