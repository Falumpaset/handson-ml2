package de.immomio.model.repository.core.landlord.customer.property.followup;

import de.immomio.data.landlord.entity.property.followup.FollowupNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseFollowupNotificationRepository extends JpaRepository<FollowupNotification, Long> {

    @Query("SELECT n FROM FollowupNotification n where n.date between :low and :high and n.followup.state <> 'PROCESSED' and n.sent = false")
    List<FollowupNotification> findAllForNotification(@Param("low") Date low, @Param("high") Date high);

    @Transactional
    @Modifying
    @Query("update FollowupNotification n set n.sent = true where n in (:notifications)")
    void setRead(@Param("notifications") List<FollowupNotification> notifications);
}
