package de.immomio.broker.service.property.portal;

import de.immomio.broker.service.BrokerGeoCodingService;
import de.immomio.broker.service.publishlog.PublishLogService;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.property.publish.PublishState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.messaging.container.PropertyBrokerContainer;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.landlord.customer.property.portal.BaseLandlordPropertyPortalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class PropertyPortalMessageService {

    private final BasePropertyRepository basePropertyRepository;

    private final BaseLandlordPropertyPortalRepository baseLandlordPropertyPortalRepository;

    private final PublishLogService publishLogService;

    private final PropertyPortalPublishService publishService;

    private final BrokerGeoCodingService geoCodingService;

    @Autowired
    public PropertyPortalMessageService(
            BasePropertyRepository basePropertyRepository,
            BaseLandlordPropertyPortalRepository baseLandlordPropertyPortalRepository,
            PublishLogService publishLogService,
            PropertyPortalPublishService publishService,
            BrokerGeoCodingService geoCodingService) {
        this.basePropertyRepository = basePropertyRepository;
        this.baseLandlordPropertyPortalRepository = baseLandlordPropertyPortalRepository;
        this.publishLogService = publishLogService;
        this.publishService = publishService;
        this.geoCodingService = geoCodingService;
    }

    public void onMessage(PropertyBrokerContainer propertyBrokerContainer) {
        Property property = null;
        LandlordProduct product = null;

        if (propertyBrokerContainer.getPropertyId() != null) {
            log.info("FlatID " + propertyBrokerContainer.getPropertyId());
            property = basePropertyRepository.findById(propertyBrokerContainer.getPropertyId()).orElse(null);
        }

        if (property == null) {
            log.info("Cancel preparation -> flat is null");
            return;
        }
        product = property.getCustomer().getActiveProduct().getProduct();

        PropertyTask propertyTask = property.getTask();
        if (product == null &&
                (property.getTask() == PropertyTask.ACTIVATE || propertyTask == PropertyTask.UPDATE)) {
            log.info("Cancel preparation -> product is null");
            return;
        }

        if (property.getPortals().isEmpty() && !propertyTask.equals(PropertyTask.DELETE)) {
            log.info("Cancel preparation -> missing portals ");
            return;
        }

        log.info("Broker preparation for flat: " + property.getData().getName());

        LandlordCustomer customer = property.getCustomer();
        log.info("Loading related customer: " + customer.getName());

        LandlordUser user = property.getUser();
        setPending(property.getPortals());

        log.info("Portals: " + property.getPortals().toString());
        log.info("Working in context of: " + user.getEmail());
        log.info("TaskType: " + property.getTask());

        PublishLog publishLog = publishLogService.createPublishLog(property);

        try {
            switch (propertyTask) {
                case ACTIVATE:
                case UPDATE:
                    geoCodingService.generateAndSaveCoordinates(Collections.singletonList(property), false);
                    publishService.activateFlat(property, publishLog);
                    break;

                case DEACTIVATE:
                    publishService.deactivateFlat(property, publishLog);
                    break;

                case DELETE:
                    publishService.deleteFlat(property, publishLog);
                break;

                default:
                    handleUnknown(property);
                    break;

            }
            if (publishLog.getPublishState() == PublishState.PENDING) {
                publishLog.setPublishState(PublishState.SUCCESS);
            }
        } catch (Exception e) {
            publishService.handleErrorState(property, publishLog, e);
        } finally {
            if (propertyTask != PropertyTask.DELETE) {
                publishLogService.savePublishLog(publishLog);
            }
        }
    }

    private void handleUnknown(Property property) {
        log.error("Unknown Operation, cannot export the flat [" +
                property.getId().toString() + "] -> " + property.getTask());
        setIdleState(property);
    }

    private void setIdleState(Property property) {
        property.setTask(PropertyTask.IDLE);
        basePropertyRepository.save(property);
    }

    private void setPending(List<PropertyPortal> portals) {
        if (portals != null && !portals.isEmpty()) {
            portals.forEach(portal -> portal.setState(PropertyPortalState.PENDING));
            baseLandlordPropertyPortalRepository.saveAll(portals);
        }
    }
}
