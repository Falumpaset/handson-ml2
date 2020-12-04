package de.immomio.model.repository.service.landlord.customer.property.portal;

import de.immomio.model.repository.core.landlord.customer.property.portal.BaseLandlordPropertyPortalRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-propertyPortals")
public interface LandlordPropertyPortalRepository extends BaseLandlordPropertyPortalRepository {

}
