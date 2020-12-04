package de.immomio.model.abstractrepository.customer.user;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;

import java.util.Optional;
import java.util.Set;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "users")
public interface BaseAbstractUserRepository<U extends AbstractUser>
        extends JpaRepository<U, Long> {

    @Override
    Optional<U> findById(@Param("id") Long id);

    @Query("SELECT o FROM #{#entityName} o WHERE o.enabled = :enabled")
    Page<U> findByEnabled(@Param("enabled") Boolean enabled, Pageable pageable);

    @Override
    @Query("SELECT o FROM #{#entityName} o")
    Page<U> findAll(Pageable pageable);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("user") U user);

    @Override <T extends U> T save(@P("user") T user);

    U findByEmail(@Param("email") String email);

    @Query("SELECT o FROM #{#entityName} o WHERE LOWER(o.email) LIKE CONCAT('%', LOWER(:email), '%')")
    Set<U> findByEmailContainingIgnoreCase(@Param("email") String email);

    @RestResource(exported = false)
    U findByEmailIgnoreCase(String email);
}
