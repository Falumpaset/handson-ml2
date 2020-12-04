package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.MappedSuperclass;

/**
 * @author Bastian Bliemeister
 */
@MappedSuperclass
@RepositoryRestResource(path = "productBasketProducts")
public interface BaseAbstractProductBasketProductRepository<PBP extends BaseProductBasketProduct<?, ?, ?>>
        extends JpaRepository<PBP, Long> {

}
