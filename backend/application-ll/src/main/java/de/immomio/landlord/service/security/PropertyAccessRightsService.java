package de.immomio.landlord.service.security;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import org.springframework.stereotype.Service;


/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyAccessRightsService {

    public boolean isPropertyAccessAllowed(Property property, LandlordUser landlordUser) {
        return landlordUser.getCustomer().equals(property.getCustomer());
    }
}
