package de.immomio.model.abstractrepository.product.addon;

import de.immomio.data.base.entity.addonproduct.AbstractAddonProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;

/**
 * @author Bastian Bliemeister
 */
@MappedSuperclass
@RepositoryRestResource(path = "addonProducts")
public interface AbstractAddonProductRepository<AP extends AbstractAddonProduct> extends JpaRepository<AP, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("addonProduct") AP addonProduct);

}
