package de.immomio.model.repository.core.landlord.customer.property.followup;

import de.immomio.data.landlord.entity.property.followup.Followup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseFollowupRepository extends JpaRepository<Followup, Long> {
}
