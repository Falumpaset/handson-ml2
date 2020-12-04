package de.immomio.model.repository.service.landlord.price;

import de.immomio.model.repository.core.landlord.price.BaseLandlordPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-prices")
public interface PriceRepository extends BaseLandlordPriceRepository {

}
