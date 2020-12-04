package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.MappedSuperclass;

/**
 * @author Bastian Bliemeister
 */
@MappedSuperclass
@RepositoryRestResource(path = "productBaskets")
public interface BaseAbstractProductBasketRepository<PB extends BaseProductBasket<?, ?, ?, ?>>
        extends JpaRepository<PB, Long> {

}
