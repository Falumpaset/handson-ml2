package de.immomio.model.repository.propertysearcher.permission.product;

import de.immomio.data.propertysearcher.entity.permission.product.PropertySearcherProductPermissionScheme;
import de.immomio.model.abstractrepository.permission.product.AbstractProductPermissionSchemaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "productPermissionSchemes")
public interface PropertySearcherProductPermissionSchemeRepository
        extends AbstractProductPermissionSchemaRepository<PropertySearcherProductPermissionScheme> {

}
