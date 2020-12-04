package de.immomio.model.repository.core.landlord.price;

import de.immomio.data.landlord.entity.price.LandlordPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 *@author Maik Baumbach
 */
@RepositoryRestResource(path = "prices")
public interface BaseLandlordPriceRepository extends JpaRepository<LandlordPrice, Long> {

}
