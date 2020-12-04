package de.immomio.model.repository.propertysearcher.user.change.password;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangePassword;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.changepassword.ChangePasswordRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherChangePasswordRepositoryImpl
        implements ChangePasswordRepositoryCustom<PropertySearcherUser, PropertySearcherChangePassword> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherChangePasswordRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherChangePassword.class);
    }

    @Override
    @Transactional
    public PropertySearcherChangePassword customSave(PropertySearcherChangePassword changePassword) {
        if (changePassword.isNew()) {
            entityManager.persist(changePassword);
        } else {
            entityManager.merge(changePassword);
        }

        return changePassword;
    }

    @Override
    public PropertySearcherChangePassword customFindOne(Long id) {
        return entityManager.find(PropertySearcherChangePassword.class, id);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherChangePassword changePassword) {
        entityManager.remove(changePassword);
    }
}
