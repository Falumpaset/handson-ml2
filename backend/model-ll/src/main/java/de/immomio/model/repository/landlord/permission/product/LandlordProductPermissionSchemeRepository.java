package de.immomio.model.repository.landlord.permission.product;

import de.immomio.data.landlord.entity.permissionscheme.productpermissionscheme.LandlordProductPermissionScheme;
import de.immomio.model.abstractrepository.permission.product.AbstractProductPermissionSchemaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "productPermissionSchemes")
public interface LandlordProductPermissionSchemeRepository
        extends AbstractProductPermissionSchemaRepository<LandlordProductPermissionScheme> {

}
