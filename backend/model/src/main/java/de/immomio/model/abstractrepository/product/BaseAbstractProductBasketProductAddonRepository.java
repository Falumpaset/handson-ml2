package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProductAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "productBasketProductAddons")
public interface BaseAbstractProductBasketProductAddonRepository<PBPA extends BaseProductBasketProductAddon<?, ?, ?>>
        extends JpaRepository<PBPA, Long> {

}
