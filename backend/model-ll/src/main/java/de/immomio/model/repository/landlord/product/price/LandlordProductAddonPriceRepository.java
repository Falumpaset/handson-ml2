package de.immomio.model.repository.landlord.product.price;

import de.immomio.data.landlord.entity.product.productaddonprice.LandlordProductAddonPrice;
import de.immomio.model.abstractrepository.product.price.AbstractProductAddonPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productAddonPrices")
public interface LandlordProductAddonPriceRepository
        extends AbstractProductAddonPriceRepository<LandlordProductAddonPrice> {

}
