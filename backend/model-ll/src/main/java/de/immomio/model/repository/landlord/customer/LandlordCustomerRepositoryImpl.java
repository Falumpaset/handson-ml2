package de.immomio.model.repository.landlord.customer;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.abstractrepository.customer.CustomerRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister
 */
public class LandlordCustomerRepositoryImpl implements CustomerRepositoryCustom<LandlordCustomer> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordCustomerRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordCustomer.class);
    }

    @Override
    @Transactional
    public LandlordCustomer customSave(LandlordCustomer object) {
        if (object.isNew()) {
            entityManager.persist(object);
        } else {
            entityManager.merge(object);
        }

        return object;
    }

    @Override
    public LandlordCustomer customFindOne(Long id) {
        return entityManager.find(LandlordCustomer.class, id);
    }

    @Override
    @Transactional
    public void customDelete(LandlordCustomer object) {
        entityManager.remove(object);
    }

}
