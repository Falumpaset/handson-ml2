package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProductAddon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "productBasketProductAddons")
public interface AbstractProductBasketProductAddonRepository<PBPA extends BaseProductBasketProductAddon<?, ?, ?>>
        extends BaseAbstractProductBasketProductAddonRepository<PBPA>, ProductBasketProductAddonRepositoryCustom<PBPA> {

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.productBasket p" +
            " WHERE o.id = :id AND p.customer.id = ?#{principal.customer.id}")
    Optional<PBPA> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.productBasket p WHERE p.customer.id = ?#{principal.customer.id}")
    Page<PBPA> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("productAddon") PBPA productAddon);

    @Override
    @PreAuthorize("#productAddon.productBasket.customer.id == principal.customer.id") <PBPAX extends PBPA> PBPAX save(
            @Param("productAddon") PBPAX productAddon);

}
