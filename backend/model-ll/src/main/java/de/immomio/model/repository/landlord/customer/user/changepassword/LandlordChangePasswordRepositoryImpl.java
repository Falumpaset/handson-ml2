/**
 *
 */
package de.immomio.model.repository.landlord.customer.user.changepassword;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.model.abstractrepository.customer.user.changepassword.ChangePasswordRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister.
 */
public class LandlordChangePasswordRepositoryImpl
        implements ChangePasswordRepositoryCustom<LandlordUser, ChangePassword> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordChangePasswordRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(ChangeEmail.class);
    }

    @Override
    @Transactional
    public ChangePassword customSave(ChangePassword changePassword) {
        if (changePassword.isNew()) {
            entityManager.persist(changePassword);
        } else {
            entityManager.merge(changePassword);
        }

        return changePassword;
    }

    @Override
    public ChangePassword customFindOne(Long id) {
        return entityManager.find(ChangePassword.class, id);
    }

    @Override
    @Transactional
    public void customDelete(ChangePassword changePassword) {
        entityManager.remove(changePassword);
    }
}
