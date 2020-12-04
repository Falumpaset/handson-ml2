package de.immomio.model.repository.landlord.product.basket;

import de.immomio.data.landlord.entity.product.basket.LandlordProductBasket;
import de.immomio.model.abstractrepository.product.ProductBasketRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class LandlordProductBasketRepositoryImpl implements ProductBasketRepositoryCustom<LandlordProductBasket> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordProductBasketRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordProductBasket.class);
    }

    @Override
    @Transactional
    public LandlordProductBasket customSave(LandlordProductBasket productBasket) {
        if (productBasket.isNew()) {
            entityManager.persist(productBasket);
        } else {
            entityManager.merge(productBasket);
        }
        return productBasket;
    }

    @Override
    public LandlordProductBasket customFindOne(Long id) {
        return entityManager.find(LandlordProductBasket.class, id);
    }

    @Override
    @Transactional
    public void customDelete(LandlordProductBasket productBasket) {
        entityManager.remove(productBasket);
    }

}
