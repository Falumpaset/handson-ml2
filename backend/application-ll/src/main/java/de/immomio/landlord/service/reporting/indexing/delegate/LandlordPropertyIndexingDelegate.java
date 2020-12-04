package de.immomio.landlord.service.reporting.indexing.delegate;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.reporting.indexing.LandlordPropertyIndexingSenderService;
import de.immomio.landlord.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class LandlordPropertyIndexingDelegate extends BaseLandlordIndexingDelegate {

    private LandlordPropertyIndexingSenderService landlordPropertyIndexingService;

    @Autowired
    public LandlordPropertyIndexingDelegate(
            LandlordPropertyIndexingSenderService landlordPropertyIndexingService,
            UserSecurityService userSecurityService
    ) {
        super(userSecurityService);
        this.landlordPropertyIndexingService = landlordPropertyIndexingService;
    }

    public void propertyCreated(Property property) {
        landlordPropertyIndexingService.propertyCreated(property, getPrincipal());
    }

    public void propertyUpdated(Property property) {
        landlordPropertyIndexingService.propertyUpdated(property, getPrincipal());
    }

    public void propertyRented(Property property) {
        landlordPropertyIndexingService.propertyRented(property, getPrincipal());
    }

    public void propertyPublished(Property property) {
        landlordPropertyIndexingService.propertyPublished(property, getPrincipal());
    }

    public void propertyUnpublished(Property property) {
        landlordPropertyIndexingService.propertyUnpublished(property, getPrincipal());
    }

    public void propertyDeleted(Property property) {
        landlordPropertyIndexingService.propertyDeleted(property, getPrincipal());
    }

    public void propertyExposeSent(Property property, String recipient, String message) {
        landlordPropertyIndexingService.propertyExposeSent(property, getPrincipal(), recipient, message);
    }
}
