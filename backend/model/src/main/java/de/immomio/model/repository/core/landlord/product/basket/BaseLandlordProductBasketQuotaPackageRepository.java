package de.immomio.model.repository.core.landlord.product.basket;

import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketQuotaPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */

@Repository
public interface BaseLandlordProductBasketQuotaPackageRepository extends JpaRepository<LandlordProductBasketQuotaPackage, Long> {
}
