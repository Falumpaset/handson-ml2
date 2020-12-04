package de.immomio.model.repository.landlord.product.customer;

import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Maik Kingma
 */

public class LandlordCustomerProductRepositoryImpl implements LandlordCustomerProductRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public LandlordCustomerProductRepositoryImpl(JpaContext context) {
        entityManager = context.getEntityManagerByManagedType(LandlordCustomerProduct.class);
    }

    @Override
    @Transactional
    public void customSave(LandlordCustomerProduct object) {
        if (object.isNew()) {
            entityManager.persist(object);
        } else {
            entityManager.merge(object);
        }
    }
}
