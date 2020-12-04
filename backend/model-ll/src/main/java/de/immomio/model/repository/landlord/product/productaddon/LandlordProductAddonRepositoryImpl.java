package de.immomio.model.repository.landlord.product.productaddon;

import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;

/**
 * @author Maik Kingma
 */

public class LandlordProductAddonRepositoryImpl implements LandlordProductAddonRepositoryCustom {
    private final EntityManager entityManager;

    @Autowired
    public LandlordProductAddonRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(LandlordProductAddon.class);
    }

    @Override
    public LandlordProductAddon customFindOne(Long id) {
        return entityManager.find(LandlordProductAddon.class, id);
    }
}
