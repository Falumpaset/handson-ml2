package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasket;
import org.springframework.data.repository.query.Param;

public interface ProductBasketRepositoryCustom<PB extends BaseProductBasket<?, ?, ?, ?>> {

    PB customSave(PB productBasket);

    PB customFindOne(@Param("id") Long id);

    void customDelete(PB changePassword);
}
