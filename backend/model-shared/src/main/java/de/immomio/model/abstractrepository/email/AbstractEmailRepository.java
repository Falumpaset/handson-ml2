package de.immomio.model.abstractrepository.email;

import de.immomio.data.base.entity.email.AbstractEmail;
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
@RepositoryRestResource(path = "emails")
public interface AbstractEmailRepository<E extends AbstractEmail<?, ?>> extends BaseAbstractEmailRepository<E> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.user.id = ?#{principal.id}")
    Optional<E> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.user.id = ?#{principal.id}")
    Page<E> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#email?.user?.id == principal?.id")
    void delete(@Param("email") E email);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#email?.user?.id == principal?.id") <T extends E> T save(@Param("email") T email);
}
