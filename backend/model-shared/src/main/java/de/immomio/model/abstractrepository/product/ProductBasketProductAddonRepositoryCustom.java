package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProductAddon;
import org.springframework.data.repository.query.Param;

public interface ProductBasketProductAddonRepositoryCustom<PBPA extends BaseProductBasketProductAddon<?, ?, ?>> {

    PBPA customSave(PBPA productBasketAddon);

    PBPA customFindOne(@Param("id") Long id);

    void customDelete(PBPA changePassword);

}
