package de.immomio.model.repository.core.landlord.customer.property.portal;

import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "propertyPortals")
public interface BaseLandlordPropertyPortalRepository extends JpaRepository<PropertyPortal, Long> {

}
