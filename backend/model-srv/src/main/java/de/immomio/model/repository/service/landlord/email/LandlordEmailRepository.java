package de.immomio.model.repository.service.landlord.email;

import de.immomio.model.repository.core.landlord.email.BaseLandlordEmailRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-emails")
public interface LandlordEmailRepository extends BaseLandlordEmailRepository {

}
