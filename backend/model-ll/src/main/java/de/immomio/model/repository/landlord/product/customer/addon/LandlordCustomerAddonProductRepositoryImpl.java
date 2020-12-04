package de.immomio.model.repository.landlord.product.customer.addon;

import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Maik Kingma
 */

public class LandlordCustomerAddonProductRepositoryImpl implements LandlordCustomerAddonProductRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public LandlordCustomerAddonProductRepositoryImpl(JpaContext context) {
        entityManager = context.getEntityManagerByManagedType(LandlordCustomerAddonProduct.class);
    }

    @Override
    @Transactional
    public void customSave(LandlordCustomerAddonProduct object) {
        if (object.isNew()) {
            entityManager.persist(object);
        } else {
            entityManager.merge(object);
        }
    }
}
