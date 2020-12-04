package de.immomio.model.abstractrepository.customer.user.changeemail;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.entity.customer.user.changeemail.AbstractChangeEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Bastian Bliemeister.
 */
@MappedSuperclass
@RepositoryRestResource(exported = false)
public interface AbstractChangeEmailRepository<U extends AbstractUser, T extends AbstractChangeEmail<U>>
        extends BaseAbstractChangeEmailRepository<U, T>, ChangeEmailRepositoryCustom<U, T> {

    @Override
    @PostAuthorize("returnObject?.get()?.user.id == principal?.id")
    Optional<T> findById(@P("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.user.id = ?#{principal.id}")
    Page<T> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@P("id") Long id);

    @Override
    @PreAuthorize("#changeEmail?.user?.id == principal?.id")
    void delete(@P("changeEmail") T changeEmail);

    @Override
    @PreAuthorize("#changeEmail?.user?.id == principal?.id") <S extends T> S save(@P("changeEmail") S changeEmail);

    @Override
    @RestResource(exported = false)
    @Query("SELECT ce FROM #{#entityName} ce WHERE lower(ce.email) = lower(:email)")
    T findByEmail(@Param("email") String email);

    @Override
    @RestResource(exported = false)
    @Query("SELECT ce FROM #{#entityName} ce WHERE ce.token = :token")
    T findByToken(@Param("token") String token);

    @Override
    @RestResource(exported = false)
    @Query("SELECT ce FROM #{#entityName} ce WHERE ce.user = :user")
    List<T> findByUser(@Param("user") U user);

    @Override
    @RestResource(exported = false)
    List<T> findByValidUntilLessThan(Date validUntil);
}
