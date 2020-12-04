package de.immomio.model.repository.core.landlord.product.addon;

import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "addonProducts")
public interface BaseAddonProductRepository extends JpaRepository<LandlordAddonProduct, Long> {

}
