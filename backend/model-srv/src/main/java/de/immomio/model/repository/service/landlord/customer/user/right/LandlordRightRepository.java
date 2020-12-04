package de.immomio.model.repository.service.landlord.customer.user.right;

import de.immomio.model.repository.core.landlord.customer.user.right.BaseLandlordRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-rights")
public interface LandlordRightRepository extends BaseLandlordRightRepository {

}
