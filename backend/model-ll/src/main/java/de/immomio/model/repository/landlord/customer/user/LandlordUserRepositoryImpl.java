/**
 *
 */
package de.immomio.model.repository.landlord.customer.user;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.model.abstractrepository.customer.user.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister.
 */
public class LandlordUserRepositoryImpl implements UserRepositoryCustom<LandlordUser> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordUserRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordUser.class);
    }

    @Override
    @Transactional
    public LandlordUser customSave(LandlordUser user) {
        return save(user, false);
    }

    @Override
    @Transactional
    public LandlordUser customSave(LandlordUser user, boolean flush) {
        return save(user, flush);
    }

    private LandlordUser save(LandlordUser user, boolean flush) {
        if (user.isNew()) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
        if (flush) {
            entityManager.flush();
        }
        return user;
    }

    @Override
    @Transactional
    public void customDelete(LandlordUser user) {
        entityManager.remove(user);
    }

    @Override
    public LandlordUser customFindOne(Long id) {
        return entityManager.find(LandlordUser.class, id);
    }
}
