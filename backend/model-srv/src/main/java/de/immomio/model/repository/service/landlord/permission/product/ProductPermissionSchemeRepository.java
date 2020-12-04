package de.immomio.model.repository.service.landlord.permission.product;

import de.immomio.model.repository.core.landlord.permission.product.BaseLandlordProductPermissionSchemeRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ll-productPermissionSchemes")
public interface ProductPermissionSchemeRepository extends BaseLandlordProductPermissionSchemeRepository {

}
