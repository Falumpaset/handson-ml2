package de.immomio.model.repository.core.landlord.customer.user;

import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */

@Repository
public interface BaseLandlordExternalApiUserRepository extends JpaRepository<LandlordExternalApiUser, Long> {
}
