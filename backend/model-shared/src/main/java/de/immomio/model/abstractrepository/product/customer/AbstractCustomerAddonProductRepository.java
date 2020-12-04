package de.immomio.model.abstractrepository.product.customer;

import de.immomio.model.abstractrepository.product.BaseAbstractCustomerAddonProductRepository;
import de.immomio.data.base.entity.customer.addonproduct.AbstractCustomerAddonProduct;
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
@RepositoryRestResource(path = "customerAddonProducts")
public interface AbstractCustomerAddonProductRepository<CAP extends AbstractCustomerAddonProduct>
        extends BaseAbstractCustomerAddonProductRepository<CAP> {

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.customerProduct cp WHERE o.id = :id AND cp.customer.id = ?#{principal.customer.id}")
    Optional<CAP> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.customerProduct cp WHERE cp.customer.id = ?#{principal.customer.id}")
    Page<CAP> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#customerAddonProduct?.customerProduct?.customer?.id == principal?.customer?.id")
    void delete(@Param("customerAddonProduct") CAP customerAddonProduct);

    @Override
    @PreAuthorize("#customerAddonProduct?.customerProduct?.customer?.id == principal?.customer?.id")
    <T extends CAP> T save(@Param("customerAddonProduct") T customerAddonProduct);
}
