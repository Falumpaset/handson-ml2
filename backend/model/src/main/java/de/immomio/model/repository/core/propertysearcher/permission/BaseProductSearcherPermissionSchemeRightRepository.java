package de.immomio.model.repository.core.propertysearcher.permission;

import de.immomio.data.propertysearcher.entity.permission.PropertySearcherPermissionSchemeRight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseProductSearcherPermissionSchemeRightRepository
        extends JpaRepository<PropertySearcherPermissionSchemeRight, Long> {
}
