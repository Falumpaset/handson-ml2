package de.immomio.model.repository.landlord.permission.addon;

import de.immomio.data.landlord.entity.permissionscheme.addonproductpermissionscheme.LandlordAddonProductPermissionScheme;
import de.immomio.model.abstractrepository.permission.addon.AbstractAddonProductPermissionSchemaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "addonProductPermissionSchemes")
public interface LandlordAddonProductPermissionSchemeRepository
        extends AbstractAddonProductPermissionSchemaRepository<LandlordAddonProductPermissionScheme> {

}
