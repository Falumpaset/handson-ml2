package de.immomio.service.reporting;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class PropertySearcherApplicationIndexingDelegate extends BasePropertySearcherIndexingDelegate {

    @Autowired
    public PropertySearcherApplicationIndexingDelegate(
            UserSecurityService userSecurityService,
            PropertySearcherIndexingSenderService propertySearcherIndexingService
    ) {
        super(userSecurityService, propertySearcherIndexingService);
    }

    public void selfDisclosureCreated(PropertyApplication application) {
        propertySearcherIndexingService.selfDisclosureCreated(application, getPrincipal().getMainProfile());
    }

    public void selfDisclosureUpdated(PropertyApplication application) {
        propertySearcherIndexingService.selfDisclosureUpdated(application, getPrincipal().getMainProfile());
    }

    public void applicationDeleted(PropertyApplication application) {
        propertySearcherIndexingService.applicationDeleted(application, getPrincipal().getMainProfile());
    }

    public void appliedPool(PropertyApplication application) {
        propertySearcherIndexingService.appliedPool(application, getPrincipal().getMainProfile());
    }

    public void appliedExternal(PropertyApplication application) {
        propertySearcherIndexingService.appliedExternal(application, application.getUserProfile());
    }

    //we sometimes have no principal here
    public void intentGiven(PropertyApplication application) {
        propertySearcherIndexingService.intentGiven(application, application.getUserProfile());
    }

    public void noIntentGiven(PropertyApplication application) {
        propertySearcherIndexingService.noIntentGiven(application, application.getUserProfile());
    }

    public void acceptedAppointment(PropertyApplication application) {
        propertySearcherIndexingService.acceptedAppointment(application, application.getUserProfile());
    }

    public void cancelledAppointment(PropertyApplication application) {
        propertySearcherIndexingService.cancelledAppointment(application, application.getUserProfile());
    }

    public void profileDataChanged(PropertyApplication application) {
        propertySearcherIndexingService.profileDataChanged(application, getPrincipal().getMainProfile());
    }

    public void profileChanged(PropertyApplication application) {
        propertySearcherIndexingService.profileChanged(application, application.getUserProfile());
    }

}
