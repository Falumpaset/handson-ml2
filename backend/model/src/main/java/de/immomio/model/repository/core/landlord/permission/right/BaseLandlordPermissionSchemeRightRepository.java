package de.immomio.model.repository.core.landlord.permission.right;

import de.immomio.data.landlord.entity.permissionscheme.permissionschemeright.LandlordPermissionSchemeRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "permissionSchemeRights")
public interface BaseLandlordPermissionSchemeRightRepository
        extends JpaRepository<LandlordPermissionSchemeRight, Long> {

}
