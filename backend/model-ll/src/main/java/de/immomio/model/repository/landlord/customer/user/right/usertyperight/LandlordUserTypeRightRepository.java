package de.immomio.model.repository.landlord.customer.user.right.usertyperight;

import de.immomio.data.landlord.entity.user.right.usertyperight.LandlordUsertypeRight;
import de.immomio.model.repository.core.landlord.customer.user.right.usertype.BaseLandlordUserTypeRightRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Maik Baumbach
 */
public interface LandlordUserTypeRightRepository extends BaseLandlordUserTypeRightRepository {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("userTypeRight") LandlordUsertypeRight userTypeRight);

    @Override
    @RestResource(exported = false) <T extends LandlordUsertypeRight> T save(@Param("userTypeRight") T userTypeRight);
}
