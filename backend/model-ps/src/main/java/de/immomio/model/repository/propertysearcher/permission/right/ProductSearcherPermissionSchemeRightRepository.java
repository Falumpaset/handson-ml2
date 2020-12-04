package de.immomio.model.repository.propertysearcher.permission.right;

import de.immomio.data.propertysearcher.entity.permission.PropertySearcherPermissionSchemeRight;
import de.immomio.model.abstractrepository.permission.right.AbstractPermissionSchemaRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "permissionSchemeRights")
public interface ProductSearcherPermissionSchemeRightRepository
        extends AbstractPermissionSchemaRightRepository<PropertySearcherPermissionSchemeRight> {

}
