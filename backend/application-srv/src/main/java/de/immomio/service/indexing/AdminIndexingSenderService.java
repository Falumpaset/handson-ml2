package de.immomio.service.indexing;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.AppointmentEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.model.event.customer.AppointmentEvent;
import de.immomio.reporting.model.event.customer.PropertyEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Service
public class AdminIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    private static final String MIGRATION = "MIGRATION";

    @Autowired
    public AdminIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    @Async
    public void propertyCreated(Property property) {
        indexProperty(property, PropertyEventType.PROPERTY_CREATED);
    }

    @Async
    public void propertyPublished(Property property, Date publishDate) {
        PropertyEvent propertyEvent = createPropertyEvent(property, getReportingEditorBean(), PropertyEventType.PROPERTY_PUBLISHED);
        propertyEvent.setTimestamp(publishDate);
        processPropertyIndexing(property.getCustomer(), propertyEvent);
    }
    @Async
    public void propertyUnpublished(Property property, Date unpublishDate) {
        PropertyEvent propertyEvent = createPropertyEvent(property, getReportingEditorBean(), PropertyEventType.PROPERTY_UNPUBLISHED);
        propertyEvent.setTimestamp(unpublishDate);
        processPropertyIndexing(property.getCustomer(), propertyEvent);
    }

    @Async
    public void appliedPool(PropertyApplication application) {
        indexApplication(application, ApplicationEventType.APPLIED_POOL);
    }

    @Async
    public void appliedExternal(PropertyApplication application) {
        indexApplication(application, ApplicationEventType.APPLIED_EXTERNAL);
    }

    @Async
    public void applicationAccepted(PropertyApplication application) {
        ApplicationEvent event = createApplicationEvent(application, getReportingEditorBean(), ApplicationEventType.APPLICATION_ACCEPTED);
        event.setTimestamp(application.getAccepted());
        processApplicationIndexing(application.getProperty().getCustomer(), event);
    }

    @Async
    public void applicationAcceptedAsTenant(PropertyApplication application, Date tenantCreatedDate) {
        ApplicationEvent event = createApplicationEvent(application, getReportingEditorBean(), ApplicationEventType.ACCEPTED_AS_TENANT);
        event.setTimestamp(tenantCreatedDate);
        processApplicationIndexing(application.getProperty().getCustomer(), event);

        PropertyEvent propertyEvent = createPropertyEvent(application.getProperty(), getReportingEditorBean(), PropertyEventType.PROPERTY_RENTED);
        propertyEvent.setTimestamp(tenantCreatedDate);
        processPropertyIndexing(application.getProperty().getCustomer(), propertyEvent);
    }

    @Async
    public void proposalCreated(PropertyProposal propertyProposal) {
        indexProposal(propertyProposal, ProposalEventType.PROPOSAL_CREATED);
    }

    @Async
    public void proposalOffered(PropertyProposal propertyProposal) {
        ProposalEvent proposalEvent = createProposalEvent(propertyProposal, getReportingEditorBean(), ProposalEventType.PROPOSAL_OFFERED);
        proposalEvent.setTimestamp(propertyProposal.getOffered());
        processProposalIndexing(propertyProposal.getProperty().getCustomer(), proposalEvent);
    }

    @Async
    public void appointmentCreated(Appointment appointment) {
        indexAppointment(appointment, AppointmentEventType.APPOINTMENT_CREATED);
    }

    @Override
    protected PropertyEvent createPropertyEvent(Property property, ReportingEditorBean editor, PropertyEventType eventType) {
        PropertyEvent propertyEvent = super.createPropertyEvent(property, editor, eventType);
        propertyEvent.setTimestamp(property.getCreated());
        return propertyEvent;
    }

    @Override
    protected AppointmentEvent createAppointmentEvent(Appointment appointment, ReportingEditorBean editor, AppointmentEventType eventType) {
        AppointmentEvent appointmentEvent = super.createAppointmentEvent(appointment, editor, eventType);
        appointmentEvent.setTimestamp(appointment.getCreated());
        return appointmentEvent;
    }

    @Override
    protected ApplicationEvent createApplicationEvent(PropertyApplication application, ReportingEditorBean editor, ApplicationEventType eventType) {
        ApplicationEvent applicationEvent = super.createApplicationEvent(application, editor, eventType);
        applicationEvent.setTimestamp(application.getCreated());
        return applicationEvent;
    }

    @Override
    protected ProposalEvent createProposalEvent(PropertyProposal proposal, ReportingEditorBean editor, ProposalEventType eventType) {
        ProposalEvent proposalEvent = super.createProposalEvent(proposal, editor, eventType);
        proposalEvent.setTimestamp(proposal.getCreated());
        return proposalEvent;
    }

    private void indexProperty(Property property, PropertyEventType eventType) {
        PropertyEvent event = createPropertyEvent(property, getReportingEditorBean(), eventType);
        processPropertyIndexing(property.getCustomer(), event);
    }

    private void indexApplication(PropertyApplication application, ApplicationEventType eventType) {
        ApplicationEvent event = createApplicationEvent(application, getReportingEditorBean(), eventType);
        processApplicationIndexing(application.getProperty().getCustomer(), event);
    }


    private void indexProposal(PropertyProposal proposal, ProposalEventType eventType) {
        ReportingEditorBean editor = getReportingEditorBean();
        ProposalEvent event = createProposalEvent(proposal, editor, eventType);
        processProposalIndexing(proposal.getProperty().getCustomer(), event);
    }

    private void indexAppointment(Appointment appointment, AppointmentEventType eventType) {
        AppointmentEvent event = createAppointmentEvent(appointment, getReportingEditorBean(), eventType);
        processAppointmentIndexing(appointment.getProperty().getCustomer(), event);
    }

    private ReportingEditorBean getReportingEditorBean() {
        ReportingEditorBean editor = new ReportingEditorBean();
        editor.setFirstName(MIGRATION);
        editor.setName(MIGRATION);
        return editor;
    }

}
