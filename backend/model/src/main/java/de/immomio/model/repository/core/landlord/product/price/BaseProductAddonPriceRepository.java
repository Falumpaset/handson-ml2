package de.immomio.model.repository.core.landlord.product.price;

import de.immomio.data.landlord.entity.product.productaddonprice.LandlordProductAddonPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productAddonPrices")
public interface BaseProductAddonPriceRepository extends JpaRepository<LandlordProductAddonPrice, Long> {

}
