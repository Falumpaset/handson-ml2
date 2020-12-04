package de.immomio.model.repository.propertysearcher.customer;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.model.abstractrepository.customer.CustomerRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherCustomerRepositoryImpl implements CustomerRepositoryCustom<PropertySearcherCustomer> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherCustomerRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherCustomer.class);
    }

    @Override
    @Transactional
    public PropertySearcherCustomer customSave(PropertySearcherCustomer object) {
        if (object.isNew()) {
            entityManager.persist(object);
        } else {
            entityManager.merge(object);
        }
        entityManager.flush();

        return object;
    }

    @Override
    public PropertySearcherCustomer customFindOne(Long id) {
        return entityManager.find(PropertySearcherCustomer.class, id);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherCustomer object) {
        entityManager.remove(object);
    }
}
