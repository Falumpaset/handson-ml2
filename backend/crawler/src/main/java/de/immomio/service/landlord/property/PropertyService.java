package de.immomio.service.landlord.property;

import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyService {

    private BasePropertyRepository basePropertyRepository;

    @Autowired
    public PropertyService(BasePropertyRepository basePropertyRepository) {
        this.basePropertyRepository = basePropertyRepository;
    }

    public Long count() {
        return basePropertyRepository.count();
    }
}
