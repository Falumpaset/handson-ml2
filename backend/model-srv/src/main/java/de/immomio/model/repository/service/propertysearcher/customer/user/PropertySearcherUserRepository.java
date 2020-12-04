package de.immomio.model.repository.service.propertysearcher.customer.user;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-users")
public interface PropertySearcherUserRepository extends BasePropertySearcherUserRepository {


    @Query(value = "select * " +
            "from propertysearcher.user u " +
            "where u.id in (select u1.user_id " +
            "               from propertysearcher.user_profile u1 " +
            "               where u1.email || ' ' || (u1.data ->> 'firstname') || ' ' || (u1.data ->> 'name')" +
            "            ilike '%' || :searchterm || '%') ", nativeQuery = true)
    @RestResource(exported = false)
    Page<PropertySearcherUser> searchUsers(@Param("searchterm") String searchterm, Pageable pageable);

}
