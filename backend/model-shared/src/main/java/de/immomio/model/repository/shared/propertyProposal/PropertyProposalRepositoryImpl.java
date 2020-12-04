package de.immomio.model.repository.shared.propertyProposal;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author Maik Kingma
 */

public class PropertyProposalRepositoryImpl implements PropertyProposalRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public PropertyProposalRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(PropertyProposal.class);
    }

    @Override
    public PropertyProposal customFindOne(Long id) {
        return entityManager.find(PropertyProposal.class, id);
    }

    @Override
    @Transactional
    public void customDeleteAllByUserProfile(PropertySearcherUserProfile userProfile) {
        Query q = entityManager.createNativeQuery("DELETE FROM shared.propertyproposal " +
                        "WHERE user_profile_id = ? " +
                        "AND state != 'OFFERED' " +
                        "AND state != 'ACCEPTED';",
                PropertyProposal.class);
        q.setParameter(1, userProfile.getId());
        q.executeUpdate();
    }

    @Override
    @Transactional
    public PropertyProposal customSave(PropertyProposal propertyProposal) {
        if (propertyProposal.isNew()) {
            entityManager.persist(propertyProposal);
        } else {
            entityManager.merge(propertyProposal);
        }

        return propertyProposal;
    }
}
