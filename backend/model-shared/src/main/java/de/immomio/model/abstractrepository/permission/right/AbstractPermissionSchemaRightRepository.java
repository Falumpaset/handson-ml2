package de.immomio.model.abstractrepository.permission.right;

import de.immomio.data.base.entity.permission.right.AbstractPermissionSchemeRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RepositoryRestResource(path = "permissionSchemes")
public interface AbstractPermissionSchemaRightRepository<PSR extends AbstractPermissionSchemeRight>
        extends JpaRepository<PSR, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("permissionSchemeRight") PSR permissionSchemeRight);

    @Override
    @RestResource(exported = false) <T extends PSR> T save(@Param("permissionSchemeRight") T permissionSchemeRight);

}
