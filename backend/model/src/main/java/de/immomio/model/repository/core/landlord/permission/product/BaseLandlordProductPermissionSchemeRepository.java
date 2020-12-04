package de.immomio.model.repository.core.landlord.permission.product;

import de.immomio.data.landlord.entity.permissionscheme.productpermissionscheme.LandlordProductPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */

@RepositoryRestResource(path = "productPermissionSchemes")
public interface BaseLandlordProductPermissionSchemeRepository
        extends JpaRepository<LandlordProductPermissionScheme, Long> {

}
