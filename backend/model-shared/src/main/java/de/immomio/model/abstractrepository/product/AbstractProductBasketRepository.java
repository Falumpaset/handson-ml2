package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasket;
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
@RepositoryRestResource(path = "productBaskets")
public interface AbstractProductBasketRepository<PB extends BaseProductBasket<?, ?, ?, ?>>
        extends BaseAbstractProductBasketRepository<PB>, ProductBasketRepositoryCustom<PB> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.customer.id = ?#{principal.customer.id}")
    Optional<PB> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<PB> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("productBasket") PB productBasket);

    @Override
    @PreAuthorize("#productBasket.customer.id == principal.customer.id") <PBX extends PB> PBX save(
            @Param("productBasket") PBX productBasket);

}
