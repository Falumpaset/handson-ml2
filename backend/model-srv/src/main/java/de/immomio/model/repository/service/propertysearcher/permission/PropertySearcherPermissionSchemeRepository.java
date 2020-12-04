package de.immomio.model.repository.service.propertysearcher.permission;

import de.immomio.model.repository.core.propertysearcher.permission.BasePropertySearcherPermissionSchemeRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-permissionSchemes")
public interface PropertySearcherPermissionSchemeRepository extends BasePropertySearcherPermissionSchemeRepository {

}
