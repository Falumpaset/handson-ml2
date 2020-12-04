package de.immomio.model.repository.service.shared.tenant;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.model.repository.core.shared.tenant.BasePropertyTenantRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "propertyTenants")
public interface PropertyTenantRepository extends BasePropertyTenantRepository {

    Optional<PropertyTenant> findByUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property);
}
