package de.immomio.model.abstractrepository.product.quota;

import de.immomio.data.base.entity.product.quota.AbstractQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */

@Repository
public interface BaseAbstractQuotaRepository<Q extends AbstractQuota<?, ?>> extends JpaRepository<Q, Long> {
}
