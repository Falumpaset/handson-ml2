package de.immomio.model.repository.landlord.product.basket;

import de.immomio.data.landlord.entity.product.basket.LandlordProductBasketProductAddon;
import de.immomio.model.abstractrepository.product.ProductBasketProductAddonRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class LandlordProductBasketProductAddonRepositoryImpl
        implements ProductBasketProductAddonRepositoryCustom<LandlordProductBasketProductAddon> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordProductBasketProductAddonRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordProductBasketProductAddon.class);
    }

    @Override
    @Transactional
    public LandlordProductBasketProductAddon customSave(LandlordProductBasketProductAddon productBasketAddon) {
        if (productBasketAddon.isNew()) {
            entityManager.persist(productBasketAddon);
        } else {
            entityManager.merge(productBasketAddon);
        }
        return productBasketAddon;

    }

    @Override
    public LandlordProductBasketProductAddon customFindOne(Long id) {
        return entityManager.find(LandlordProductBasketProductAddon.class, id);
    }

    @Override
    @Transactional
    public void customDelete(LandlordProductBasketProductAddon productBasketAddon) {
        entityManager.remove(productBasketAddon);
    }

}
