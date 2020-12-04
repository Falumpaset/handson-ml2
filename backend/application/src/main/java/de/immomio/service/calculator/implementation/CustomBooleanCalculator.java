package de.immomio.service.calculator.implementation;

import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer, Maik Kingma
 */

@Service
public class CustomBooleanCalculator {

    public double calculateWbs(AdditionalInformation additionalInformation) {
        if (userHasWbs(additionalInformation)) {
            return 1.0;
        }
        return 0.0;
    }

    private boolean userHasWbs(AdditionalInformation info) {
        return info != null && info.getWbs() != null && info.getWbs();
    }
}
