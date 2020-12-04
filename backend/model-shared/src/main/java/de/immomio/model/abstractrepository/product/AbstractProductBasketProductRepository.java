package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.MappedSuperclass;
import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@MappedSuperclass
@RepositoryRestResource(path = "productBasketProducts")
public interface AbstractProductBasketProductRepository<PBP extends BaseProductBasketProduct<?, ?, ?>>
        extends BaseAbstractProductBasketProductRepository<PBP>, ProductBasketProductRepositoryCustom<PBP> {

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.productBasket p" +
            " WHERE o.id = :id AND p.customer.id = ?#{principal.customer.id}")
    Optional<PBP> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.productBasket p WHERE p.customer.id = ?#{principal.customer.id}")
    Page<PBP> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("productBasketProduct") PBP productBasketProduct);

    @Override
    @PreAuthorize("#productBasketProduct.productBasket.customer.id == principal.customer.id")
    <PBPX extends PBP> PBPX save(@Param("productBasketProduct") PBPX productBasketProduct);

}
