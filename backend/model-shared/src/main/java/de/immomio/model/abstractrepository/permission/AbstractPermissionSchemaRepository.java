package de.immomio.model.abstractrepository.permission;

import de.immomio.data.base.entity.permission.AbstractPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RepositoryRestResource(path = "permissionSchemes")
public interface AbstractPermissionSchemaRepository<PS extends AbstractPermissionScheme>
        extends JpaRepository<PS, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("permissionScheme") PS permissionScheme);

    @Override
    @RestResource(exported = false) <T extends PS> T save(@Param("permissionScheme") T permissionScheme);

}
