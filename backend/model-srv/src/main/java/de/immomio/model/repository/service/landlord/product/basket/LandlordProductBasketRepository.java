package de.immomio.model.repository.service.landlord.product.basket;

import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productBaskets")
public interface LandlordProductBasketRepository extends BaseLandlordProductBasketRepository {

}
