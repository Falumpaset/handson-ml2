package de.immomio.model.repository.service.landlord.permission.right;

import de.immomio.model.repository.core.landlord.permission.right.BaseLandlordPermissionSchemeRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-permissionSchemeRights")
public interface PermissionSchemeRightRepository extends BaseLandlordPermissionSchemeRightRepository {

}
