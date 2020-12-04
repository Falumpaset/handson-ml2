package de.immomio.model.repository.landlord.customer.user;

import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUser;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordExternalApiUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Repository
public interface LandlordExternalApiUserRepository extends BaseLandlordExternalApiUserRepository {

    @Query("SELECT u from LandlordExternalApiUser u where lower(u.username) = :username")
    Optional<LandlordExternalApiUser> findFirstByUsername(@Param("username") String username);

    @Override
    @Query("SELECT u from LandlordExternalApiUser u where u.customer = ?#{principal.customer}")
    Page<LandlordExternalApiUser> findAll(Pageable pageable);
}
