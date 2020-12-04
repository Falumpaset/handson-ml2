package de.immomio.model.repository.core.landlord.customer.user.settings;

import de.immomio.data.landlord.entity.user.settings.LandlordUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseLandlordUserSettingsRepository extends JpaRepository<LandlordUserSettings, Long> {

}
