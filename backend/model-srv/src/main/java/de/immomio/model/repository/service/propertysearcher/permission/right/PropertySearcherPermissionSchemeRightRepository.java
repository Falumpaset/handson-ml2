package de.immomio.model.repository.service.propertysearcher.permission.right;

import de.immomio.model.repository.core.propertysearcher.permission.BaseProductSearcherPermissionSchemeRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-permissionSchemeRights")
public interface PropertySearcherPermissionSchemeRightRepository extends
        BaseProductSearcherPermissionSchemeRightRepository {

}
