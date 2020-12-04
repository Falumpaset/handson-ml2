package de.immomio.service.reporting;

import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertySearcherIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    @Autowired
    public PropertySearcherIndexingSenderService(RabbitTemplate template) {
       super(template);
    }

    @Async
    public void selfDisclosureCreated(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.SD_CREATED, userProfile);
    }

    @Async
    public void selfDisclosureUpdated(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.SD_UPDATED, userProfile);
    }

    @Async
    public void applicationDeleted(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.APPLICATION_DELETED, userProfile);
    }

    @Async
    public void appliedPool(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.APPLIED_POOL, userProfile);
    }

    @Async
    public void appliedExternal(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.APPLIED_EXTERNAL, userProfile);
    }

    @Async
    public void intentGiven(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.INTENT_GIVEN, userProfile);
    }

    @Async
    public void noIntentGiven(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.NO_INTENT_GIVEN, userProfile);
    }

    @Async
    public void acceptedAppointment(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.APPOINTMENT_ACCEPTED, userProfile);
    }

    @Async
    public void cancelledAppointment(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.APPOINTMENT_CANCELED_BY_PS, userProfile);
    }

    @Async
    public void profileDataChanged(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.PROFILE_DATA_CHANGED, userProfile);
    }

    @Async
    public void profileChanged(PropertyApplication application, PropertySearcherUserProfile userProfile) {
        indexApplication(application, ApplicationEventType.PROFILE_CHANGED, userProfile);
    }



    @Async
    public void proposalDenied(PropertyProposal propertyProposal, PropertySearcherUserProfile userProfile) {
        ProposalEvent event = createProposalEvent(
                propertyProposal,
                new ReportingEditorBean(userProfile),
                ProposalEventType.PROPOSAL_DENIED_BY_PS);
        processProposalIndexing(propertyProposal.getProperty().getCustomer(), event);
    }

    @Async
    public void proposalAccepted(PropertyProposal propertyProposal, PropertySearcherUserProfile userProfile) {
        ProposalEvent event = createProposalEvent(
                propertyProposal,
                new ReportingEditorBean(userProfile),
                ProposalEventType.PROPOSAL_ACCEPTED);
        processProposalIndexing(propertyProposal.getProperty().getCustomer(), event);
    }

    private void indexApplication(PropertyApplication application, ApplicationEventType eventType, PropertySearcherUserProfile userProfile) {
        ApplicationEvent event = createApplicationEvent(application, new ReportingEditorBean(userProfile), eventType);
        processApplicationIndexing(application.getProperty().getCustomer(), event);
    }

}
