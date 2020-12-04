package de.immomio.model.repository.core.landlord.permission.addon;

import de.immomio.data.landlord.entity.permissionscheme.addonproductpermissionscheme.LandlordAddonProductPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "addonProductPermissionSchemes")
public interface BaseLandlordAddonProductPermissionSchemeRepository
        extends JpaRepository<LandlordAddonProductPermissionScheme, Long> {

}
