package de.immomio.model.repository.service.landlord.permission.addon;

import de.immomio.model.repository.core.landlord.permission.addon.BaseLandlordAddonProductPermissionSchemeRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-addonProductPermissionSchemes")
public interface AddonProductPermissionSchemeRepository extends BaseLandlordAddonProductPermissionSchemeRepository {

}
