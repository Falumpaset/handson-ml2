package de.immomio.model.repository.shared.application;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Maik Kingma
 */

public class PropertyApplicationRepositoryImpl implements PropertyApplicationRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public PropertyApplicationRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertyApplication.class);
    }

    @Override
    public PropertyApplication customFindOne(Long id) {
        return entityManager.find(PropertyApplication.class, id);
    }

    @Override
    @Transactional
    public PropertyApplication customSave(PropertyApplication propertyApplication) {
        if (propertyApplication.isNew()) {
            entityManager.persist(propertyApplication);
        } else {
            entityManager.merge(propertyApplication);
        }

        return propertyApplication;
    }
}
