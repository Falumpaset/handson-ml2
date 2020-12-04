package de.immomio.model.repository.core.landlord.product.price;

import de.immomio.data.landlord.entity.product.productprice.LandlordProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productPrices")
public interface BaseProductPriceRepository extends JpaRepository<LandlordProductPrice, Long> {

}
