package de.immomio.model.repository.landlord.product;

import de.immomio.data.landlord.entity.product.LandlordProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;

public class LandlordProductRepositoryImpl implements LandlordProductRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public LandlordProductRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordProduct.class);
    }

    @Override
    public LandlordProduct customFindOne(Long id) {
        return entityManager.find(LandlordProduct.class, id);
    }
}
