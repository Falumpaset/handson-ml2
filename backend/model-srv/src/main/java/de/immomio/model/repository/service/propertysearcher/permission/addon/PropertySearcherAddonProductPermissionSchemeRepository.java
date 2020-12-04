package de.immomio.model.repository.service.propertysearcher.permission.addon;

import de.immomio.model.repository.core.propertysearcher.permission.addon.BasePropertySearcherAddonProductPermissionSchemeRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-addonProductPermissionSchemes")
public interface PropertySearcherAddonProductPermissionSchemeRepository
        extends BasePropertySearcherAddonProductPermissionSchemeRepository {

}
