package de.immomio.model.abstractrepository.customer.user.changeemail;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.entity.customer.user.changeemail.AbstractChangeEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.method.P;

import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Bastian Bliemeister.
 */
@MappedSuperclass
@RepositoryRestResource(exported = false)
public interface BaseAbstractChangeEmailRepository<U extends AbstractUser, T extends AbstractChangeEmail<U>>
        extends JpaRepository<T, Long> {

    @Override
    Optional<T> findById(@P("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o")
    Page<T> findAll(Pageable pageable);

    @Override
    void deleteById(@P("id") Long id);

    @Override
    void delete(@P("changeEmail") T changeEmail);

    @Override <S extends T> S save(@P("changeEmail") S changeEmail);

    @Query("SELECT ce FROM #{#entityName} ce WHERE lower(ce.email) = lower(:email)")
    T findByEmail(@Param("email") String email);

    @Query("SELECT ce FROM #{#entityName} ce WHERE ce.token = :token")
    T findByToken(@Param("token") String token);

    @Query("SELECT ce FROM #{#entityName} ce WHERE ce.user = :user")
    List<T> findByUser(@Param("user") U user);

    List<T> findByValidUntilLessThan(Date validUntil);
}
