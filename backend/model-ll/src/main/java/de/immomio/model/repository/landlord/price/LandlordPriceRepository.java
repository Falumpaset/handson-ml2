package de.immomio.model.repository.landlord.price;

import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.model.abstractrepository.price.AbstractPriceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 *@author Maik Baumbach
 */
@RepositoryRestResource(path = "prices")
public interface LandlordPriceRepository extends AbstractPriceRepository<LandlordPrice> {

}
