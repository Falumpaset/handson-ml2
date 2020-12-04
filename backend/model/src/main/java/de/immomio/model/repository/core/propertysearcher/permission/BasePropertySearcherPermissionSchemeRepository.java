package de.immomio.model.repository.core.propertysearcher.permission;

import de.immomio.data.propertysearcher.entity.permission.PropertySearcherPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherPermissionSchemeRepository
        extends JpaRepository<PropertySearcherPermissionScheme, Long> {

}
