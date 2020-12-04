package de.immomio.model.repository.service.landlord.product.price;

import de.immomio.model.repository.core.landlord.product.price.BaseProductAddonPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productAddonPrices")
public interface ProductAddonPriceRepository extends BaseProductAddonPriceRepository {

}
