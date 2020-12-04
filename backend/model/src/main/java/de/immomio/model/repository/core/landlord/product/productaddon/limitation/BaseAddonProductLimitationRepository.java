package de.immomio.model.repository.core.landlord.product.productaddon.limitation;

import de.immomio.data.landlord.entity.product.addon.limitation.LandlordAddonProductLimitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "addonProductLimitations")
public interface BaseAddonProductLimitationRepository extends JpaRepository<LandlordAddonProductLimitation, Long> {

}
