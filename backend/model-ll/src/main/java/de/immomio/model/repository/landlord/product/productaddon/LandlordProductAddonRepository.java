package de.immomio.model.repository.landlord.product.productaddon;

import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.model.repository.core.landlord.product.productaddon.BaseLandlordProductAddonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productAddons")
public interface LandlordProductAddonRepository
        extends BaseLandlordProductAddonRepository,
        LandlordProductAddonRepositoryCustom {

    @Query("SELECT o from LandlordProductAddon o where o.product = :product")
    List<LandlordProductAddon> findAvailableByProduct(@Param("product") LandlordProduct product);
}
