package de.immomio.broker.service;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PropertyService {

    private final BasePropertyRepository propertyRepository;

    @Autowired
    public PropertyService(BasePropertyRepository basePropertyRepository) {
        this.propertyRepository = basePropertyRepository;
    }

    public void disableAutoOffer(Property property) {
        if (property.isAutoOfferEnabled()) {
            property.setAutoOfferEnabled(false);
            propertyRepository.save(property);
        }
    }
}
