package de.immomio.model.abstractrepository.product.customer;

import de.immomio.model.abstractrepository.product.BaseAbstractCustomerProductRepository;
import de.immomio.data.base.entity.customer.AbstractCustomer;
import de.immomio.data.base.entity.customer.AbstractCustomerProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "customerProducts")
public interface AbstractCustomerProductRepository<CP extends AbstractCustomerProduct, C extends AbstractCustomer>
        extends BaseAbstractCustomerProductRepository<CP, C> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.customer.id = ?#{principal.customer.id}")
    Optional<CP> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<CP> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#customerProduct?.customer?.id == principal?.customer?.id")
    void delete(@Param("customerProduct") CP customerProduct);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#customerProduct?.customer?.id == principal?.customer?.id") <T extends CP> T save(
            @Param("customerProduct") T customerProduct);

    @Override
    @RestResource(exported = false)
    Set<CP> findByRenewAndDueDateIsBefore(Boolean renew, Date dueDate);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    List<CP> findByCustomerAndDueDateIsAfter(C customer, Date dueDate);
}
