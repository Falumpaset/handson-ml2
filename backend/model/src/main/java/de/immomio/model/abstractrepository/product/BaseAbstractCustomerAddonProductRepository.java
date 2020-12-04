package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.customer.addonproduct.AbstractCustomerAddonProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "customerAddonProducts")
public interface BaseAbstractCustomerAddonProductRepository<CAP extends AbstractCustomerAddonProduct>
        extends JpaRepository<CAP, Long> {

    @RestResource(exported = false)
    @Query("SELECT a from #{#entityName} a where a.customerProduct.id = :customerProduct and a.addonProduct.id = :addonProduct and renew = true")
    List<CAP> findFirstByCustomerProductRenew(@Param("customerProduct") Long customerProduct, @Param("addonProduct") Long addonProduct, Pageable limit);
}
