package de.immomio.model.abstractrepository.product;

import de.immomio.data.base.entity.product.basket.BaseProductBasketProduct;
import org.springframework.data.repository.query.Param;

public interface ProductBasketProductRepositoryCustom<PBP extends BaseProductBasketProduct<?, ?, ?>> {

    PBP customSave(PBP productBasketProduct);

    PBP customFindOne(@Param("id") Long id);

    void customDelete(PBP changePassword);
}
