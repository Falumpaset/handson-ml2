package de.immomio.model.repository.landlord.product.basket;

import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProduct;
import de.immomio.model.abstractrepository.product.ProductBasketProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class LandlordProductBasketProductRepositoryImpl
        implements ProductBasketProductRepositoryCustom<LandlordProductBasketProduct> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordProductBasketProductRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordProductBasketProduct.class);
    }

    @Override
    @Transactional
    public LandlordProductBasketProduct customSave(LandlordProductBasketProduct productBasketProduct) {
        if (productBasketProduct.isNew()) {
            entityManager.persist(productBasketProduct);
        } else {
            entityManager.merge(productBasketProduct);
        }
        return productBasketProduct;
    }

    @Override
    public LandlordProductBasketProduct customFindOne(Long id) {
        return entityManager.find(LandlordProductBasketProduct.class, id);
    }

    @Override
    @Transactional
    public void customDelete(LandlordProductBasketProduct productBasketProduct) {
        entityManager.remove(productBasketProduct);
    }

}
