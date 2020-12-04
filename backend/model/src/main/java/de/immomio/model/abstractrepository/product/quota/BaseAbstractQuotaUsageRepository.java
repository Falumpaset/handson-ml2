package de.immomio.model.abstractrepository.product.quota;

import de.immomio.data.base.entity.product.quota.AbstractQuotaUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseAbstractQuotaUsageRepository<QU extends AbstractQuotaUsage<?, ?>> extends JpaRepository<QU, Long> {
}
