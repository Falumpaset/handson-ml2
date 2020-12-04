/**
 *
 */
package de.immomio.model.repository.core.landlord.customer.user.right.usertype;

import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.right.usertyperight.LandlordUsertypeRight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Maik Baumbach
 */
public interface BaseLandlordUserTypeRightRepository extends JpaRepository<LandlordUsertypeRight, Long> {

    List<LandlordUsertypeRight> findAllByUserType(LandlordUsertype userType);

}
