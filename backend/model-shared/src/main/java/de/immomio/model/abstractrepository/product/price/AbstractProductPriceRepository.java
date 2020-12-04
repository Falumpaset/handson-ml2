package de.immomio.model.abstractrepository.product.price;

import de.immomio.data.base.entity.product.price.AbstractProductPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RepositoryRestResource(path = "productPrices")
public interface AbstractProductPriceRepository<PP extends AbstractProductPrice> extends JpaRepository<PP, Long> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.customer.id = ?#{principal.customer.id}")
    Optional<PP> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<PP> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#productPrice?.customer?.id == principal?.customer?.id")
    void delete(@Param("productPrice") PP productPrice);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#productPrice?.customer?.id == principal?.customer?.id") <T extends PP> T save(
            @Param("productPrice") T productPrice);
}
