package de.immomio.landlord.service.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertyIndexingDelegate;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.RabbitMQService;
import de.immomio.service.landlord.property.AbstractPropertyUpdatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Maik Kingma, Valeriy Navozenko
 */

@Service
public class PropertyUpdatingService extends AbstractPropertyUpdatingService<PropertyRepository> {

    private final PropertyRepository propertyRepository;

    private LandlordPropertyIndexingDelegate propertyIndexingDelegate;

    @Autowired
    public PropertyUpdatingService(
            LandlordPropertyIndexingDelegate propertyIndexingDelegate,
            PropertyRepository propertyRepository, RabbitMQService rabbitMQService
    ) {
        super(rabbitMQService);
        this.propertyIndexingDelegate = propertyIndexingDelegate;
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

    @Override
    protected void savePublishedEvent(Property property) {
        propertyIndexingDelegate.propertyPublished(property);
    }

    @Override
    protected void saveUnpublishedEvent(Property property) {
        propertyIndexingDelegate.propertyUnpublished(property);
    }

    @Override
    protected void saveDeletedEvent(Property property) {
        propertyIndexingDelegate.propertyDeleted(property);
    }
}
