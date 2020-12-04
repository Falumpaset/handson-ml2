package de.immomio.model.repository.service.landlord.product.basket;

import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productBasketProducts")
public interface LandlordProductBasketProductRepository extends BaseLandlordProductBasketProductRepository {

}
