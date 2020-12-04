package de.immomio.model.repository.service.propertysearcher.product.basket;

import de.immomio.model.repository.core.propertysearcher.product.basket.BasePropertySearcherProductBasketProductRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-productBasketProducts")
public interface PropertySearcherProductBasketProductRepository
        extends BasePropertySearcherProductBasketProductRepository {

}