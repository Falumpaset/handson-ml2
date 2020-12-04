package de.immomio.service.landlord.property;

import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.service.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Maik Kingma
 */
@Slf4j
public abstract class AbstractPropertyUpdatingService<T extends JpaRepository<Property, Long>> {

    private final RabbitMQService rabbitMQService;

    private static final String PROPERTY_NOT_FOUND_EVENT_MSG = "Property with ID {} not found. Event {} did not send";

    public abstract T getPropertyRepository();

    public abstract void saveProperty(Property property);

    public abstract void saveProperties(List<Property> properties);

    protected abstract void savePublishedEvent(Property property);

    protected abstract void saveUnpublishedEvent(Property property);

    protected abstract void saveDeletedEvent(Property property);


    public AbstractPropertyUpdatingService(RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    public void publishToPortals(Long propertyId) {
        Optional<Property> propertyOpt = getPropertyRepository().findById(propertyId);

        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();

            updateTaskAndSend(property, PropertyTask.ACTIVATE);
            savePublishedEvent(property);
        } else {
            propertyNotFoundErrorMessage(propertyId, PropertyTask.ACTIVATE);
        }
    }

    public void updateForPortals(Long propertyId) {
        Optional<Property> propertyOpt = getPropertyRepository().findById(propertyId);

        if (propertyOpt.isPresent()) {
            updateTaskAndSend(propertyOpt.get(), PropertyTask.UPDATE);
        } else {
            propertyNotFoundErrorMessage(propertyId, PropertyTask.UPDATE);
        }
    }

    public void deactivateForPortals(Long propertyId) {
        Optional<Property> propertyOpt = getPropertyRepository().findById(propertyId);

        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();

            updateTaskAndSend(property, PropertyTask.DEACTIVATE);
            saveUnpublishedEvent(property);
        } else {
            propertyNotFoundErrorMessage(propertyId, PropertyTask.DEACTIVATE);
        }
    }

    public void deleteProperties(List<Property> properties) {
        properties.forEach(property -> property.setTask(PropertyTask.DELETE));
        saveProperties(properties);

        properties.forEach(rabbitMQService::queueProperty);
    }

    public void deactivateFromPortalsThenDeleteProperty(Long propertyId) {
        Optional<Property> propertyOpt = getPropertyRepository().findById(propertyId);

        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            updateTaskAndSend(property, PropertyTask.DELETE);
            saveDeletedEvent(property);
        } else {
            propertyNotFoundErrorMessage(propertyId, PropertyTask.DELETE);
        }
    }

    private void updateTaskAndSend(Property property, PropertyTask propertyTask) {
        property.setTask(propertyTask);
        saveProperty(property);

        rabbitMQService.queueProperty(property);
    }

    private void propertyNotFoundErrorMessage(Long propertyId, PropertyTask task) {
        log.error(PROPERTY_NOT_FOUND_EVENT_MSG, propertyId, task.name());
    }

}
