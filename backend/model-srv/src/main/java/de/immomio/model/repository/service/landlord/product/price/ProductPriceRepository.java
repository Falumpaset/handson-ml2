package de.immomio.model.repository.service.landlord.product.price;

import de.immomio.model.repository.core.landlord.product.price.BaseProductPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productPrices")
public interface ProductPriceRepository extends BaseProductPriceRepository {

}
