package de.immomio.landlord.service.reporting.indexing;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.PropertyEvent;
import de.immomio.reporting.model.event.customer.PropertyNotificationEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordPropertyIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    @Autowired
    public LandlordPropertyIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    @Async
    public void propertyCreated(Property property, LandlordUser user) {
        indexProperty(property, PropertyEventType.PROPERTY_CREATED, user);
    }

    @Async
    public void propertyUpdated(Property property, LandlordUser user) {
        indexProperty(property, PropertyEventType.PROPERTY_UPDATED, user);
    }

    @Async
    public void propertyRented(Property property, LandlordUser user) {
        indexProperty(property, PropertyEventType.PROPERTY_RENTED, user);
    }

    @Async
    public void propertyPublished(Property property, LandlordUser user) {
        indexProperty(property, PropertyEventType.PROPERTY_PUBLISHED, user);
    }

    @Async
    public void propertyUnpublished(Property property, LandlordUser user) {
        indexProperty(property, PropertyEventType.PROPERTY_UNPUBLISHED, user);
    }

    @Async
    public void propertyDeleted(Property property, LandlordUser user) {
        indexProperty(property, PropertyEventType.PROPERTY_DELETED, user);
    }

    @Async
    public void propertyExposeSent(Property property, LandlordUser user, String recipient, String message) {
        indexPropertyNotification(property, PropertyEventType.PROPERTY_EXPOSE_SENT, user, recipient, message);
    }

    private void indexProperty(Property property, PropertyEventType eventType, LandlordUser user) {
        PropertyEvent event = createPropertyEvent(property, new ReportingEditorBean(user), eventType);
        processPropertyIndexing(user.getCustomer(), event);
    }

    private void indexPropertyNotification(Property property, PropertyEventType eventType, LandlordUser user, String recipient, String message) {
        PropertyNotificationEvent event = createPropertyNotificationEvent(property, new ReportingEditorBean(user), eventType, recipient, message);
        processPropertyIndexing(user.getCustomer(), event);
    }

}
