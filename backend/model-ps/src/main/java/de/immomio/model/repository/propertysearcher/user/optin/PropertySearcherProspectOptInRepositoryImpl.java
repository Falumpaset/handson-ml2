package de.immomio.model.repository.propertysearcher.user.optin;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherProspectOptIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * @author Maik Kingma
 */

public class PropertySearcherProspectOptInRepositoryImpl implements PropertySearcherProspectOptInRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherProspectOptInRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherProspectOptIn.class);
    }

    @Override
    @Transactional
    public void customSave(PropertySearcherProspectOptIn optIn) {
        if (optIn.isNew()) {
            optIn.setCreated(new Date());
            entityManager.persist(optIn);
        } else {
            optIn.setUpdated(new Date());
            entityManager.merge(optIn);
        }
    }
}
