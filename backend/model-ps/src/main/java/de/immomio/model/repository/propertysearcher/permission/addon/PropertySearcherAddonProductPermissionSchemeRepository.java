package de.immomio.model.repository.propertysearcher.permission.addon;

import de.immomio.data.propertysearcher.entity.permission.addon.PropertySearcherAddonProductPermissionScheme;
import de.immomio.model.abstractrepository.permission.addon.AbstractAddonProductPermissionSchemaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "addonProductPermissionSchemes")
public interface PropertySearcherAddonProductPermissionSchemeRepository
        extends AbstractAddonProductPermissionSchemaRepository<PropertySearcherAddonProductPermissionScheme> {

}
