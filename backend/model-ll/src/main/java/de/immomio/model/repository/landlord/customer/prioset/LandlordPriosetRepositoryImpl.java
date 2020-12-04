/**
 *
 */
package de.immomio.model.repository.landlord.customer.prioset;

import de.immomio.data.landlord.entity.prioset.Prioset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Bastian Bliemeister.
 */
public class LandlordPriosetRepositoryImpl implements LandlordPriosetRepositoryCustom<Prioset> {

    private final EntityManager entityManager;

    @Autowired
    public LandlordPriosetRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(Prioset.class);
    }

    @Override
    @Transactional
    public Prioset customSave(Prioset prioset) {
        if (prioset.isNew()) {
            entityManager.persist(prioset);
        } else {
            entityManager.merge(prioset);
        }

        return prioset;
    }

    @Override
    public Prioset customFindOne(Long id) {
        return entityManager.find(Prioset.class, id);
    }
}
