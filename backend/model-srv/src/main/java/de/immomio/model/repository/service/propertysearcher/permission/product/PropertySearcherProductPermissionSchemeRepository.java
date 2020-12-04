package de.immomio.model.repository.service.propertysearcher.permission.product;

import de.immomio.model.repository.core.propertysearcher.permission.product.BasePropertySearcherProductPermissionSchemeRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "ps-productPermissionSchemes")
public interface PropertySearcherProductPermissionSchemeRepository
        extends BasePropertySearcherProductPermissionSchemeRepository {

}
