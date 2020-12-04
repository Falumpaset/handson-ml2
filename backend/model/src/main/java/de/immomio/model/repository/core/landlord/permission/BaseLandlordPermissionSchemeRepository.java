package de.immomio.model.repository.core.landlord.permission;

import de.immomio.data.landlord.entity.permissionscheme.LandlordPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "permissionSchemes")
public interface BaseLandlordPermissionSchemeRepository extends JpaRepository<LandlordPermissionScheme, Long> {

}
