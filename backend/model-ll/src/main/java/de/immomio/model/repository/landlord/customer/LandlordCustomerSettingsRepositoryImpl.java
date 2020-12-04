package de.immomio.model.repository.landlord.customer;

import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.model.abstractrepository.customer.CustomerSettingsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class LandlordCustomerSettingsRepositoryImpl
        implements CustomerSettingsRepositoryCustom<LandlordCustomerSettings> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordCustomerSettingsRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordCustomerSettings.class);
    }

    @Override
    @Transactional
    public LandlordCustomerSettings customSave(LandlordCustomerSettings customerSettings) {
        if (customerSettings.isNew()) {
            entityManager.persist(customerSettings);
        } else {
            entityManager.merge(customerSettings);
        }

        return customerSettings;
    }

}
