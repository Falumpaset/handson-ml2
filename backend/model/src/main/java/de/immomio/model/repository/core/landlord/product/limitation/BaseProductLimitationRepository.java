package de.immomio.model.repository.core.landlord.product.limitation;

import de.immomio.data.landlord.entity.product.limitation.LandlordProductLimitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Maik Baumbach
 */
@RepositoryRestResource(path = "productLimitations")
public interface BaseProductLimitationRepository extends JpaRepository<LandlordProductLimitation, Long> {

}
