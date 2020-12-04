package de.immomio.model.abstractrepository.customer.user;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;

import java.util.Optional;
import java.util.Set;

/**
 * @author Bastian Bliemeister
 */

@RepositoryRestResource(path = "users")
public interface AbstractUserRepository<U extends AbstractUser>
        extends BaseAbstractUserRepository<U>, UserRepositoryCustom<U> {

    @Override
    @PostAuthorize("returnObject?.get()?.customer.id == principal?.customer.id")
    Optional<U> findById(@Param("id") Long id);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id")
    U loadById(@Param("id") Long id);

    @Query("SELECT o FROM #{#entityName} o WHERE o.enabled = :enabled AND (o.id = ?#{principal.id} OR o.customer.id =" +
            " ?#{principal.customer.id})")
    Page<U> findByEnabled(@Param("enabled") Boolean enabled, Pageable pageable);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = ?#{principal.id} OR o.customer.id = ?#{principal.customer.id}")
    Page<U> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <T extends U> T save(@P("user") T user);

    @Query(nativeQuery = true,
            value = "SELECT * FROM landlord.user WHERE (id = ?#{principal.id} OR customer_id = ?#{principal.customer.id}) AND LOWER((profile->>'firstname')\\:\\:text || ' ' || (profile->>'name')\\:\\:text) LIKE LOWER('%' || :name || '%')")
    Set<U> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(exported = false)
    U findByEmail(@Param("email") String email);

    @RestResource(exported = false)
    U findByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT o FROM #{#entityName} o WHERE LOWER(o.email) LIKE CONCAT('%', LOWER(:email), '%') AND (o.id = ?#{principal.id} OR o.customer.id = ?#{principal.customer.id})")
    Set<U> findByEmailContainingIgnoreCase(@Param("email") String email);
}
