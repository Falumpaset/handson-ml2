package de.immomio.broker.service;

import de.immomio.cloud.service.AbstractGeoCodingService;
import de.immomio.cloud.service.google.GoogleMapsService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class BrokerGeoCodingService extends AbstractGeoCodingService {

    private final BasePropertyRepository propertyRepository;

    private final GoogleMapsService googleMapsService;

    @Autowired
    public BrokerGeoCodingService(BasePropertyRepository propertyRepository, GoogleMapsService googleMapsService) {
        this.propertyRepository = propertyRepository;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public void customUpdatePropertyLocation(Long id) {
        propertyRepository.customUpdatePropertyLocation(id);
    }

    @Override
    public GoogleMapsService getGoogleMapsService() {
        return googleMapsService;
    }
}
