/**
 *
 */
package de.immomio.service.calculator.implementation;

import de.immomio.data.landlord.entity.prioset.Prioset;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer.
 */

@Service
public class PreferencePriosetCalculator {

    public int calculatePreferencePrioset(Prioset prioset) {
        int result = 0;

        // Digital positive category
        if (prioset.getData().getAge() != null) {
            result += prioset.getData().getAge().getValue();
        }
        if (prioset.getData().getEmploymentType() != null) {
            result += prioset.getData().getEmploymentType().getValue();
        }
        if (prioset.getData().getResidents() != null) {
            result += prioset.getData().getResidents().getValue();
        }
        if (prioset.getData().getHouseholdType() != null) {
            result += prioset.getData().getHouseholdType().getValue();
        }

        if (prioset.getData().getAnimals() != null) {
            result += prioset.getData().getAnimals();
        }
        if (prioset.getData().getChildren() != null) {
            result += prioset.getData().getChildren();
        }

        // income category
        if (prioset.getData().getMonthlyIncome() != null) {
            result += prioset.getData().getMonthlyIncome().getValue();
        }

        return result;
    }
}
