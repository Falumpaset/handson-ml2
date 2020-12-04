package de.immomio.model.repository.core.landlord.product.basket;

import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.model.abstractrepository.product.BaseAbstractProductBasketProductAddonRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "productBasketProductAddons")
public interface BaseProductBasketProductAddonRepository
        extends BaseAbstractProductBasketProductAddonRepository<LandlordProductBasketProductAddon> {
}
