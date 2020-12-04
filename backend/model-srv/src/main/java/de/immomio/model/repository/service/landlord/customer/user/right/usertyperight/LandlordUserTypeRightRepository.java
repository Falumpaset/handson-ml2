package de.immomio.model.repository.service.landlord.customer.user.right.usertyperight;

import de.immomio.model.repository.core.landlord.customer.user.right.usertype.BaseLandlordUserTypeRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-userTypeRights")
public interface LandlordUserTypeRightRepository extends BaseLandlordUserTypeRightRepository {

}
