package de.immomio.model.abstractrepository.customer;

import de.immomio.data.base.entity.customer.AbstractCustomerSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Maik Kingma
 */
@RepositoryRestResource(path = "customerSettings")
public interface AbstractCustomerSettingsRepository<C extends AbstractCustomerSettings<?>>
        extends BaseAbstractCustomerSettingsRepository<C>, CustomerSettingsRepositoryCustom<C> {

    @Override
    @PreAuthorize("#settings.id == principal.customer.id")
    <S extends C> S save(@Param("settings") S settings);

    @Override
    @RestResource(exported = false)
    @PostAuthorize("returnObject?.get()?.customer?.id == principal?.customer?.id")
    Optional<C> findById(@Param("id") Long id);

    @Override
    C getOne(Long aLong);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<C> findAll(Pageable pageable);
}
