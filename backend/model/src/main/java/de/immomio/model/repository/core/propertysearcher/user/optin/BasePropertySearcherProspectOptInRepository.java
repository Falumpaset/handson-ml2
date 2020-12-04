package de.immomio.model.repository.core.propertysearcher.user.optin;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherProspectOptIn;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasePropertySearcherProspectOptInRepository extends JpaRepository<PropertySearcherProspectOptIn, Long> {

    PropertySearcherProspectOptIn findByUser(PropertySearcherUser user);

}
