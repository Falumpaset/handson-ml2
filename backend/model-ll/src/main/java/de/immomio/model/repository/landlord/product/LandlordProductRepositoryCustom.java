package de.immomio.model.repository.landlord.product;

import de.immomio.data.landlord.entity.product.LandlordProduct;

public interface LandlordProductRepositoryCustom {

    LandlordProduct customFindOne(Long id);
}
