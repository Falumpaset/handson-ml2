package de.immomio.model.repository.service.landlord.product.basket;

import de.immomio.model.repository.core.landlord.product.basket.BaseLandlordProductBasketProductAddonRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productBasketProductAddons")
public interface LandlordProductBasketProductAddonRepository extends BaseLandlordProductBasketProductAddonRepository {

}
