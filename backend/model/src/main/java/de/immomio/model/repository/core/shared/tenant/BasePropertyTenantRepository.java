package de.immomio.model.repository.core.shared.tenant;

import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "propertyTenants")
public interface BasePropertyTenantRepository extends JpaRepository<PropertyTenant, Long> {
}
