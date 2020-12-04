package de.immomio.service.reporting;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.service.security.UserSecurityService;

/**
 * @author Niklas Lindemann
 */
public abstract class BasePropertySearcherIndexingDelegate {

    protected UserSecurityService userSecurityService;

    protected PropertySearcherIndexingSenderService propertySearcherIndexingService;

    public BasePropertySearcherIndexingDelegate(
            UserSecurityService userSecurityService,
            PropertySearcherIndexingSenderService propertySearcherIndexingService
    ) {
        this.userSecurityService = userSecurityService;
        this.propertySearcherIndexingService = propertySearcherIndexingService;
    }

    protected PropertySearcherUser getPrincipal() {
        return userSecurityService.getPrincipalUser();
    }
}
