package de.immomio.model.repository.propertysearcher.permission;

import de.immomio.data.propertysearcher.entity.permission.PropertySearcherPermissionScheme;
import de.immomio.model.abstractrepository.permission.AbstractPermissionSchemaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "permissionSchemes")
public interface PropertySearcherPermissionSchemeRepository
        extends AbstractPermissionSchemaRepository<PropertySearcherPermissionScheme> {

}
