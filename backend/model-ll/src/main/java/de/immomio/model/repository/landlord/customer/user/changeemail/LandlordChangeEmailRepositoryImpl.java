/**
 *
 */
package de.immomio.model.repository.landlord.customer.user.changeemail;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.model.abstractrepository.customer.user.changeemail.ChangeEmailRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister.
 */
public class LandlordChangeEmailRepositoryImpl implements ChangeEmailRepositoryCustom<LandlordUser, ChangeEmail> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordChangeEmailRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(ChangeEmail.class);
    }

    @Override
    @Transactional
    public ChangeEmail customSave(ChangeEmail changeEmail) {
        if (changeEmail.isNew()) {
            entityManager.persist(changeEmail);
        } else {
            entityManager.merge(changeEmail);
        }

        return changeEmail;
    }

    @Override
    public ChangeEmail customFindOne(Long id) {
        return entityManager.find(ChangeEmail.class, id);
    }

    @Override
    @Transactional
    public void customDelete(ChangeEmail changeEmail) {
        entityManager.remove(changeEmail);
    }
}
