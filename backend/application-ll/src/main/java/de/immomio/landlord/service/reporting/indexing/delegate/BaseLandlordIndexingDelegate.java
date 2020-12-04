package de.immomio.landlord.service.reporting.indexing.delegate;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.security.UserSecurityService;

/**
 * @author Niklas Lindemann
 */
public abstract class BaseLandlordIndexingDelegate {

    protected UserSecurityService userSecurityService;

    public BaseLandlordIndexingDelegate(
            UserSecurityService userSecurityService
    ) {
        this.userSecurityService = userSecurityService;
    }

    protected LandlordUser getPrincipal() {
        return userSecurityService.getPrincipalUser();
    }
}
