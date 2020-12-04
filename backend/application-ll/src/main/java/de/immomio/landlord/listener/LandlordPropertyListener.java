package de.immomio.landlord.listener;

import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertyIndexingDelegate;
import de.immomio.landlord.service.sender.LandlordProposalMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler(Property.class)
public class LandlordPropertyListener {

    private final LandlordProposalMessageSender landlordProposalMessageSender;

    private final LandlordPropertyIndexingDelegate propertyIndexingDelegate;

    @Autowired
    public LandlordPropertyListener(
            LandlordProposalMessageSender landlordProposalMessageSender,
            LandlordPropertyIndexingDelegate propertyIndexingDelegate
    ) {
        this.landlordProposalMessageSender = landlordProposalMessageSender;
        this.propertyIndexingDelegate = propertyIndexingDelegate;
    }

    @HandleAfterCreate
    @HandleAfterSave
    public void updateLatLongAndProposals(Property property) {
        sendMessage(property);
    }

    @HandleAfterCreate
    public void createCreatedEvent(Property property) {
        propertyIndexingDelegate.propertyCreated(property);
    }

    private void sendMessage(Property property) {
        landlordProposalMessageSender.sendProposalUpdateMessage(property, true);
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void beforeCreateOrSave(Property property) {
        LandlordCustomer landlordCustomer = property.getCustomer();
        if (property.isShowSelfDisclosureQuestions() && !landlordCustomer.isSelfDisclosureAllowed()) {
            String errorMessage = String.format("User is not allowed to use module %s", AddonType.SELF_DISCLOSURE);
            throw new RuntimeException(errorMessage);
        }
    }
}
