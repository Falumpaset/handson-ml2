package de.immomio.model.repository.landlord.permission;

import de.immomio.data.landlord.entity.permissionscheme.LandlordPermissionScheme;
import de.immomio.model.abstractrepository.permission.AbstractPermissionSchemaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "permissionSchemes")
public interface LandlordPermissionSchemeRepository
        extends AbstractPermissionSchemaRepository<LandlordPermissionScheme> {

}
