package de.immomio.model.repository.propertysearcher.user;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class PropertySearcherUserRepositoryImpl implements UserRepositoryCustom<PropertySearcherUser> {

    private final EntityManager entityManager;

    @Autowired
    public PropertySearcherUserRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertySearcherUser.class);
    }

    @Override
    @Transactional
    public PropertySearcherUser customSave(PropertySearcherUser user) {
        if (user.isNew()) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }

        return user;
    }

    @Override
    public PropertySearcherUser customSave(PropertySearcherUser user, boolean flush) {
        return customSave(user);
    }

    @Override
    @Transactional
    public void customDelete(PropertySearcherUser user) {
        entityManager.remove(user);
    }

    @Override
    public PropertySearcherUser customFindOne(Long id) {
        return entityManager.find(PropertySearcherUser.class, id);
    }
}
