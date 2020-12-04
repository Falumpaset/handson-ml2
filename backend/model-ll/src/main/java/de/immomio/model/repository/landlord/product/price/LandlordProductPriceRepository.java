package de.immomio.model.repository.landlord.product.price;

import de.immomio.data.landlord.entity.product.productprice.LandlordProductPrice;
import de.immomio.model.abstractrepository.product.price.AbstractProductPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productPrices")
public interface LandlordProductPriceRepository extends AbstractProductPriceRepository<LandlordProductPrice> {

}
