/**
 *
 */
package de.immomio.model.repository.landlord.customer.credential;

import de.immomio.data.landlord.entity.credential.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister.
 */
public class LandlordCredentialRepositoryImpl implements LandlordCredentialRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public LandlordCredentialRepositoryImpl(JpaContext context) {
        entityManager = context.getEntityManagerByManagedType(Credential.class);
    }

    @Override
    @Transactional
    public Credential customSave(Credential object) {
        if (object.isNew()) {
            entityManager.persist(object);
        } else {
            entityManager.merge(object);
        }

        return object;
    }

    @Override
    public Credential customFindOne(Long id) {
        return entityManager.find(Credential.class, id);
    }

    @Override
    @Transactional
    public void customDelete(Credential object) {
        entityManager.remove(object);
    }
}
