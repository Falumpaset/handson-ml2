package de.immomio.model.repository.landlord.permission.right;

import de.immomio.data.landlord.entity.permissionscheme.permissionschemeright.LandlordPermissionSchemeRight;
import de.immomio.model.abstractrepository.permission.right.AbstractPermissionSchemaRightRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "permissionSchemeRights")
public interface LandlordPermissionSchemeRightRepository
        extends AbstractPermissionSchemaRightRepository<LandlordPermissionSchemeRight> {

}
