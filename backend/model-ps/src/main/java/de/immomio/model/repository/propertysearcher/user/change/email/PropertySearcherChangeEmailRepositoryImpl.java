package de.immomio.model.repository.propertysearcher.user.change.email;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangeEmail;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.changeemail.ChangeEmailRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherChangeEmailRepositoryImpl
        implements ChangeEmailRepositoryCustom<PropertySearcherUser, PropertySearcherChangeEmail> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherChangeEmailRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherChangeEmail.class);
    }

    @Override
    @Transactional
    public PropertySearcherChangeEmail customSave(PropertySearcherChangeEmail changeEmail) {
        if (changeEmail.isNew()) {
            entityManager.persist(changeEmail);
        } else {
            entityManager.merge(changeEmail);
        }

        return changeEmail;
    }

    @Override
    public PropertySearcherChangeEmail customFindOne(Long id) {
        return entityManager.find(PropertySearcherChangeEmail.class, id);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherChangeEmail changeEmail) {
        entityManager.remove(changeEmail);
    }
}
