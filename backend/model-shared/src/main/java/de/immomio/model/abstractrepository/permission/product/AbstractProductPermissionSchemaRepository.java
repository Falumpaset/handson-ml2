package de.immomio.model.abstractrepository.permission.product;

import de.immomio.data.base.entity.permission.product.AbstractProductPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RepositoryRestResource(path = "productPermissionSchemes")
public interface AbstractProductPermissionSchemaRepository<PPS extends AbstractProductPermissionScheme>
        extends JpaRepository<PPS, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("productPermissionScheme") PPS productPermissionScheme);

    @Override
    @RestResource(exported = false) <T extends PPS> T save(@Param("productPermissionScheme") T productPermissionScheme);

}
