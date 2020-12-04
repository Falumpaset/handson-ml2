package de.immomio.landlord.service.property;

import de.immomio.data.landlord.bean.property.PropertyEventBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.landlord.service.reporting.data.LandlordReportDataHandler;
import de.immomio.model.repository.landlord.publishlog.LandlordPublishLogRepository;
import de.immomio.reporting.model.event.customer.PropertyNotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertyEventService {

    private LandlordPublishLogRepository publishLogRepository;

    private LandlordReportDataHandler reportDataHandler;

    @Autowired
    public PropertyEventService(LandlordPublishLogRepository publishLogRepository, LandlordReportDataHandler reportDataHandler) {
        this.publishLogRepository = publishLogRepository;
        this.reportDataHandler = reportDataHandler;
    }

    public List<PropertyEventBean> getPropertyEvents(Property property) {
        List<PublishLog> publishLogs = publishLogRepository.findByProperty(property);
        List<PropertyNotificationEvent> notificationEvents = reportDataHandler.getPropertyNotifications(property, PropertyEventType.PROPERTY_EXPOSE_SENT);

        List<PropertyEventBean> eventBeans = Stream.concat(publishLogs.stream().map(publishLog -> PropertyEventBean.builder()
                                .created(publishLog.getCreated())
                                .eventType(publishLog.getPropertyTask().name())
                                .status(publishLog.getPublishState().name())
                                .portals(publishLog.getPortals() != null ? publishLog.getPortals().toArray(new String[0]) : null).build()),

                notificationEvents.stream().map(propertyNotificationEvent -> PropertyEventBean.builder()
                        .message(propertyNotificationEvent.getMessage())
                        .recipient(propertyNotificationEvent.getRecipient())
                        .created(propertyNotificationEvent.getTimestamp()).eventType(PropertyEventType.PROPERTY_EXPOSE_SENT.name())
                        .build()))

                .sorted(Comparator.comparing(PropertyEventBean::getCreated))
                .collect(Collectors.toList());
        return eventBeans;
    }

}
