package de.immomio.model.repository.propertysearcher.user;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.AbstractUserRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

public interface PropertySearcherUserRepository extends AbstractUserRepository<PropertySearcherUser> {

    @Override
    @RestResource(exported = false)
    PropertySearcherUser save(@Param("user") PropertySearcherUser user);

    @RestResource(exported = false)
    PropertySearcherUser findByEmail(@Param("email") String email);

    @RestResource(exported = false)
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = :id AND o.customer.id = ?#{principal.customer.id}")
    Set<PropertySearcherUser> findByCustomer(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("user") PropertySearcherUser user);

}
