package de.immomio.landlord.service.reporting.indexing.delegate;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.reporting.indexing.LandlordApplicationIndexingSenderService;
import de.immomio.landlord.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class LandlordApplicationIndexingDelegate extends BaseLandlordIndexingDelegate {

    private LandlordApplicationIndexingSenderService landlordApplicationIndexingService;

    @Autowired
    public LandlordApplicationIndexingDelegate(
            UserSecurityService userSecurityService,
            LandlordApplicationIndexingSenderService landlordApplicationIndexingService
    ) {
        super(userSecurityService);
        this.landlordApplicationIndexingService = landlordApplicationIndexingService;
    }

    public void askedForIntent(PropertyApplication application) {
        landlordApplicationIndexingService.askedForIntent(application, getPrincipal());
    }

    public void dkLevelChanged(PropertyApplication application) {
        landlordApplicationIndexingService.dkLevelChanged(application, getPrincipal());
    }

    public void applicationAccepted(PropertyApplication application) {
        landlordApplicationIndexingService.applicationAccepted(application, getPrincipal());
    }

    public void applicationRejected(PropertyApplication application) {
        landlordApplicationIndexingService.applicationRejected(application, getPrincipal());
    }

    public void acceptedAsTenant(PropertyApplication application) {
        landlordApplicationIndexingService.acceptedAsTenant(application, getPrincipal());
    }

    public void askedForSelfDisclosure(PropertyApplication application) {
        landlordApplicationIndexingService.askedForSelfDisclosure(application, getPrincipal());
    }


}
