package de.immomio.service.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.service.landlord.customer.property.PropertyRepository;
import de.immomio.service.RabbitMQService;
import de.immomio.service.landlord.property.AbstractPropertyUpdatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class AdminPropertyUpdatingService extends AbstractPropertyUpdatingService<PropertyRepository> {

    private PropertyRepository propertyRepository;

    @Autowired
    public AdminPropertyUpdatingService(RabbitMQService rabbitMQService, PropertyRepository propertyRepository) {
        super(rabbitMQService);
        this.propertyRepository = propertyRepository;
    }


    @Override
    public PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }

    @Override
    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    @Override
    public void saveProperties(List<Property> properties) {
        propertyRepository.saveAll(properties);
    }

    private static final String METHOD_NOT_IMPLEMENTED_MSG = "Method did not implement";

    // TODO: 13.11.19 Should we track admin panel events?
    @Override
    protected void savePublishedEvent(Property property) {
        log.warn(METHOD_NOT_IMPLEMENTED_MSG);
    }

    @Override
    protected void saveUnpublishedEvent(Property property) {
        log.warn(METHOD_NOT_IMPLEMENTED_MSG);
    }

    @Override
    protected void saveDeletedEvent(Property property) {
        log.warn(METHOD_NOT_IMPLEMENTED_MSG);
    }

}
