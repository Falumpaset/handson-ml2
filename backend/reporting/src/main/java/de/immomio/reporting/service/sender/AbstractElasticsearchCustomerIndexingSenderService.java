package de.immomio.reporting.service.sender;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.AppointmentEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertySearcherEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.RatingEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.reporting.model.beans.ReportingAppointmentBean;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.beans.ReportingPropertyApplicationBean;
import de.immomio.reporting.model.beans.ReportingPropertyBean;
import de.immomio.reporting.model.beans.propertysearcher.ReportingPropertySearcherBean;
import de.immomio.reporting.model.beans.propertysearcher.ReportingPropertySearcherIndexable;
import de.immomio.reporting.model.beans.propertysearcher.ReportingProposalBean;
import de.immomio.reporting.model.event.customer.AbstractCustomerEvent;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.model.event.customer.AppointmentEvent;
import de.immomio.reporting.model.event.customer.PropertyEvent;
import de.immomio.reporting.model.event.customer.PropertyNotificationEvent;
import de.immomio.reporting.model.event.customer.PropertySearcherEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.reporting.model.event.customer.RatingEvent;
import de.immomio.reporting.model.messaging.CustomerIndexingEvent;
import de.immomio.reporting.model.messaging.IndexingCustomerMessageContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchCustomerIndexingConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.ElasticsearchCustomerIndexingConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class AbstractElasticsearchCustomerIndexingSenderService {

    private static final String PROPERTY_INDEX_PREFIX = "property-";
    private static final String APPOINTMENT_INDEX_PREFIX = "appointment-";
    private static final String APPLICATION_INDEX_PREFIX = "application-";
    private static final String CURRENT_INDEX_PREFIX = "current-";

    private static final String PROPERTYSEARCHER_INDEX_PREFIX = "propertysearcher-";
    private static final String PROPOSAL_INDEX_PREFIX = "proposal-";
    private static final String RATING_INDEX_PREFIX = "rating-";
    private static final String IGNORE_MALFORMED = "ignore_malformed";
    private static final String TRUE = "true";
    private static final String DEFAULT = "default";

    private RabbitTemplate rabbitTemplate;

    public AbstractElasticsearchCustomerIndexingSenderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    protected PropertyNotificationEvent createPropertyNotificationEvent(Property property, ReportingEditorBean editor, PropertyEventType eventType, String recipient, String message) {
        PropertyNotificationEvent event = new PropertyNotificationEvent();
        event.setTimestamp(new Date());
        event.setProperty(new ReportingPropertyBean(property));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setId(property.getId());
        event.setRecipient(recipient);
        event.setMessage(message);
        return event;
    }

    protected PropertyEvent createPropertyEvent(Property property, ReportingEditorBean editor, PropertyEventType eventType) {
        PropertyEvent event = createPropertyNotificationEvent(property, editor, eventType, null, null);
        event.setTimestamp(new Date());
        event.setProperty(new ReportingPropertyBean(property));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setId(property.getId());
        return event;
    }

    protected AppointmentEvent createAppointmentEvent(Appointment appointment, ReportingEditorBean editor, AppointmentEventType eventType) {
        AppointmentEvent event = new AppointmentEvent();
        event.setTimestamp(new Date());
        event.setAppointment(new ReportingAppointmentBean(appointment));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setId(appointment.getId());
        return event;
    }

    protected ApplicationEvent createApplicationEvent(PropertyApplication application, ReportingEditorBean editor, ApplicationEventType eventType) {
        ApplicationEvent event = new ApplicationEvent();
        event.setTimestamp(new Date());
        event.setApplication(new ReportingPropertyApplicationBean(application));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setId(application.getId());
        return event;
    }

    protected ProposalEvent createProposalEvent(PropertyProposal proposal, ReportingEditorBean editor, ProposalEventType eventType) {
        ProposalEvent event = new ProposalEvent();
        event.setTimestamp(new Date());
        event.setProposal(new ReportingProposalBean(proposal));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setId(proposal.getId());
        return event;
    }

    protected PropertySearcherEvent createPropertySearcherEvent(
            PropertySearcherUserProfile userProfile,
            ReportingEditorBean editor,
            PropertySearcherEventType eventType,
            ReportingPropertySearcherIndexable data
    ) {
        PropertySearcherEvent event = new PropertySearcherEvent();
        event.setTimestamp(new Date());
        event.setUser(new ReportingPropertySearcherBean(userProfile));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setData(data);
        event.setId(userProfile.getId());
        return event;
    }

    protected RatingEvent createRatingEvent(
            PropertySearcherUserProfile userProfile,
            ReportingEditorBean editor,
            RatingEventType eventType,
            Double rating
    ) {
        RatingEvent event = new RatingEvent();
        event.setTimestamp(new Date());
        event.setUser(new ReportingPropertySearcherBean(userProfile));
        event.setEditor(editor);
        event.setEventType(eventType);
        event.setRating(rating);
        event.setId(userProfile.getId());
        return event;
    }

    protected void processPropertyIndexing(LandlordCustomer customer, PropertyEvent... events) {
        processIndexing(customer, Arrays.asList(events), PROPERTY_INDEX_PREFIX);
    }

    protected void processAppointmentIndexing(LandlordCustomer customer, AppointmentEvent... events) {
        processIndexing(customer, Arrays.asList(events), APPOINTMENT_INDEX_PREFIX);
    }

    protected void processApplicationIndexing(LandlordCustomer customer, ApplicationEvent... events) {
        processIndexing(customer, Arrays.asList(events), APPLICATION_INDEX_PREFIX);
    }

    protected void processPropertySearcherIndexing(LandlordCustomer customer, PropertySearcherEvent... events) {
        processIndexing(customer, Arrays.asList(events), PROPERTYSEARCHER_INDEX_PREFIX);
    }

    protected void processProposalIndexing(LandlordCustomer customer, ProposalEvent... events) {
        processIndexing(customer, Arrays.asList(events), PROPOSAL_INDEX_PREFIX);
    }

    protected void processRatingIndexing(LandlordCustomer customer, RatingEvent... events) {
        processIndexing(customer, Arrays.asList(events), RATING_INDEX_PREFIX);
    }

    private void processIndexing(LandlordCustomer customer, List<AbstractCustomerEvent> events, String prefix) {
        List<CustomerIndexingEvent> indexingEvents = events.stream()
                .map(event -> new CustomerIndexingEvent(event.writeJson(), event.getId()))
                .collect(Collectors.toList());

        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new IndexingCustomerMessageContainer(indexingEvents, prefix, customer.getId()));
    }
}
