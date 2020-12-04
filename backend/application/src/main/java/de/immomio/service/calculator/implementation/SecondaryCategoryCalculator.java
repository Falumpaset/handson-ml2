/**
 *
 */
package de.immomio.service.calculator.implementation;

import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Johannes Hiemer.
 */

@Slf4j
public class SecondaryCategoryCalculator {

    private static final double BAILMENT_LIMIT = 0.5;

    public static double calculateBailsman(PropertySearcherUserProfileData userProfile, double factor) {
        AdditionalInformation info = userProfile.getAdditionalInformation();
        if (info != null && info.getBailment() != null && info.getBailment() && factor < BAILMENT_LIMIT) {
            factor = BAILMENT_LIMIT;
        }

        return factor;
    }
}
