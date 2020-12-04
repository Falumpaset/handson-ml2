package de.immomio.model.abstractrepository.permission.addon;

import de.immomio.data.base.entity.permission.addon.AbstractAddonProductPermissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@RepositoryRestResource(path = "addonProductPermissionSchemes")
public interface AbstractAddonProductPermissionSchemaRepository<APPS extends AbstractAddonProductPermissionScheme>
        extends JpaRepository<APPS, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("addonProductPermissionScheme") APPS addonProductPermissionScheme);

    @Override
    @RestResource(exported = false) <T extends APPS> T save(
            @Param("addonProductPermissionScheme") T addonProductPermissionScheme);

}
