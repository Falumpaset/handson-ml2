package de.immomio.model.repository.propertysearcher.product.basket;

import de.immomio.data.propertysearcher.entity.product.basket.PropertySearcherProductBasket;
import de.immomio.model.abstractrepository.product.ProductBasketRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherProductBasketRepositoryImpl
        implements ProductBasketRepositoryCustom<PropertySearcherProductBasket> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherProductBasketRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherProductBasket.class);
    }

    @Override
    @Transactional
    public PropertySearcherProductBasket customSave(PropertySearcherProductBasket productBasket) {
        if (productBasket.isNew()) {
            entityManager.persist(productBasket);
        } else {
            entityManager.merge(productBasket);
        }
        return productBasket;
    }

    @Override
    public PropertySearcherProductBasket customFindOne(Long id) {
        return entityManager.find(PropertySearcherProductBasket.class, id);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherProductBasket productBasket) {
        entityManager.remove(productBasket);
    }

}
