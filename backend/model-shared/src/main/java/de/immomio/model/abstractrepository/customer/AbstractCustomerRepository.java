package de.immomio.model.abstractrepository.customer;

import de.immomio.data.base.entity.customer.AbstractCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "customers")
public interface AbstractCustomerRepository<C extends AbstractCustomer<?, ?>>
        extends BaseAbstractCustomerRepository<C>, CustomerRepositoryCustom<C> {

    @Override
    @RestResource(exported = false) <S extends C> S save(@P("customer") S customer);

    @Override
    @PreAuthorize("#id == principal.customer.id")
    Optional<C> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = ?#{principal.customer.id}")
    Page<C> findAll(Pageable pageable);
}
