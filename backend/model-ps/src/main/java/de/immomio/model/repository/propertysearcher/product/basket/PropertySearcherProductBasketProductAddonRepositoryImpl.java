package de.immomio.model.repository.propertysearcher.product.basket;

import de.immomio.data.propertysearcher.entity.product.basket.PropertySearcherProductBasketProductAddon;
import de.immomio.model.abstractrepository.product.ProductBasketProductAddonRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherProductBasketProductAddonRepositoryImpl
        implements ProductBasketProductAddonRepositoryCustom<PropertySearcherProductBasketProductAddon> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherProductBasketProductAddonRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherProductBasketProductAddon.class);
    }

    @Override
    @Transactional
    public PropertySearcherProductBasketProductAddon customSave(
            PropertySearcherProductBasketProductAddon productBasketAddon) {
        if (productBasketAddon.isNew()) {
            entityManager.persist(productBasketAddon);
        } else {
            entityManager.merge(productBasketAddon);
        }
        return productBasketAddon;

    }

    @Override
    public PropertySearcherProductBasketProductAddon customFindOne(Long id) {
        return entityManager.find(PropertySearcherProductBasketProductAddon.class, id);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherProductBasketProductAddon productBasketAddon) {
        entityManager.remove(productBasketAddon);
    }

}
