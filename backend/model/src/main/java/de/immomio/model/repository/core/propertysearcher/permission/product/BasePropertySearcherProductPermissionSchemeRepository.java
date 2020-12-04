package de.immomio.model.repository.core.propertysearcher.permission.product;

import de.immomio.data.propertysearcher.entity.permission.product.PropertySearcherProductPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherProductPermissionSchemeRepository
        extends JpaRepository<PropertySearcherProductPermissionScheme, Long> {

}
