package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.addonproduct.AbstractAddonProduct;
import de.immomio.data.base.entity.product.AbstractProduct;
import de.immomio.data.base.entity.product.addon.AbstractProductAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * @author Bastian Bliemeister
 */
@MappedSuperclass
@RepositoryRestResource(path = "productAddons")
public interface AbstractProductAddonRepository<PA extends AbstractProductAddon, P extends AbstractProduct,
        AP extends AbstractAddonProduct> extends JpaRepository<PA, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("productAddon") PA productAddon);

    @RestResource(exported = false)
    PA findByProductAndAddonProduct(P product, AP addonProduct);

    @RestResource(exported = false)
    List<PA> findByProduct(@Param("product") P product);

}
