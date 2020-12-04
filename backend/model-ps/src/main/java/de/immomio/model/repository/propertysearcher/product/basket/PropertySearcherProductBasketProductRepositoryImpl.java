package de.immomio.model.repository.propertysearcher.product.basket;

import de.immomio.data.propertysearcher.entity.product.basket.PropertySearcherProductBasketProduct;
import de.immomio.model.abstractrepository.product.ProductBasketProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherProductBasketProductRepositoryImpl
        implements ProductBasketProductRepositoryCustom<PropertySearcherProductBasketProduct> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherProductBasketProductRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherProductBasketProduct.class);
    }

    @Override
    @Transactional
    public PropertySearcherProductBasketProduct customSave(PropertySearcherProductBasketProduct productBasketProduct) {
        if (productBasketProduct.isNew()) {
            entityManager.persist(productBasketProduct);
        } else {
            entityManager.merge(productBasketProduct);
        }
        return productBasketProduct;
    }

    @Override
    public PropertySearcherProductBasketProduct customFindOne(Long id) {
        return entityManager.find(PropertySearcherProductBasketProduct.class, id);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherProductBasketProduct productBasketProduct) {
        entityManager.remove(productBasketProduct);
    }

}
