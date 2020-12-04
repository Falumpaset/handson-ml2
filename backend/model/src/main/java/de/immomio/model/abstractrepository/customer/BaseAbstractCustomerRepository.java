package de.immomio.model.abstractrepository.customer;

import de.immomio.data.base.entity.customer.AbstractCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.method.P;

import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "customers")
public interface BaseAbstractCustomerRepository<C extends AbstractCustomer<?, ?>>
        extends JpaRepository<C, Long> {

    @Override <S extends C> S save(@P("customer") S customer);

    @Override
    Optional<C> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o")
    Page<C> findAll(Pageable pageable);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("customer") C customer);

}
