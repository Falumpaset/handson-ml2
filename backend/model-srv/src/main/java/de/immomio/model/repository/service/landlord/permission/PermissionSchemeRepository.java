package de.immomio.model.repository.service.landlord.permission;

import de.immomio.model.repository.core.landlord.permission.BaseLandlordPermissionSchemeRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-permissionSchemes")
public interface PermissionSchemeRepository extends BaseLandlordPermissionSchemeRepository {

}
