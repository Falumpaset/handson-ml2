package de.immomio.model.repository.core.landlord.product.productaddon;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productAddons")
public interface BaseLandlordProductAddonRepository extends JpaRepository<LandlordProductAddon, Long> {

    @Query("SELECT pa from LandlordProductAddon pa where pa.addonProduct.addonType = :addonType and pa.product = :product")
    LandlordProductAddon findByProductAndAddonProductAddonType(LandlordProduct product, AddonType addonType);

}
