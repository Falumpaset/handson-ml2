package de.immomio.model.repository.service.landlord.customer.user;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-users")
public interface LandlordUserRepository extends BaseLandlordUserRepository {

    @Query(nativeQuery = true, value = "SELECT * FROM landlord.user  WHERE (jsonb_extract_path_text(profile, 'name')" +
            " = :name;")
    List<LandlordUser> findByName(@Param("name") String name);

    LandlordUser findByEmailIgnoreCase(@Param("email") String email);

    @RestResource(exported = false)
    LandlordUser findByEmailIgnoreCaseAndUsertypeAndCustomer(String email, LandlordUsertype usertype, LandlordCustomer customer);

    @RestResource(exported = false)
    List<LandlordUser> findByUsertype(LandlordUsertype usertype);

    @RestResource(exported = false)
    @Query("SELECT u FROM LandlordUser u where" +
            " concat(" +
            "lower( function('jsonb_extract_path_text', u.profile, 'firstname')),' '," +
            "lower(function('jsonb_extract_path_text', u.profile, 'name')), ' ' , " +
            "lower(u.email), ' ', " +
            "u.id, ' ' ," +
            "lower(u.customer.name), ' '," +
            "u.customer.id) like :searchTerm ")
    Page<LandlordUser> search(String searchTerm, Pageable pageable);

}
