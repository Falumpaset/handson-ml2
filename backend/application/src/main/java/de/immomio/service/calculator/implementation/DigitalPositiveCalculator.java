/**
 *
 */
package de.immomio.service.calculator.implementation;

import de.immomio.data.base.type.property.EmploymentType;
import de.immomio.data.base.type.property.HouseholdType;
import de.immomio.data.landlord.bean.prioset.BoundaryValue;
import de.immomio.data.landlord.bean.prioset.ListValue;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer, Maik Kingma
 */

@Slf4j
@Component
public class DigitalPositiveCalculator {

    public int calculateAge(Prioset prioset, PropertySearcherUserProfileData userProfileData) {
        DateTime today = new DateTime();
        PriosetData data = prioset.getData();

        if (userProfileData != null && userProfileData.getDateOfBirth() != null && data != null && data.getAge() != null) {
            DateTime dateOfBirth = new DateTime(userProfileData.getDateOfBirth().getTime());
            int age = new Period(today, dateOfBirth).getYears() * -1;
            BoundaryValue agePrioset = data.getAge();
            if (agePrioset.getLowerBound() <= age && age <= prioset.getData().getAge().getUpperBound()) {
                return 1;
            }
        }
        return 0;
    }

    public int calculateHouseholdType(Prioset prioset, PropertySearcherUserProfileData userProfile) {
        PriosetData data = prioset.getData();

        ListValue<HouseholdType> householdType = data.getHouseholdType();
        if (householdType != null
                    && householdType.getChoice() != null
                    && userProfile.getHouseholdType() != null
                    && householdType.getChoice().contains(userProfile.getHouseholdType())
        ) {
            return 1;
        }
        return 0;
    }

    public int calculateJob(Prioset prioset, PropertySearcherUserProfileData userProfile) {
        Profession profession = userProfile.getProfession();
        PriosetData data = prioset.getData();
        if (profession == null || data == null) {
            return 0;
        }

        EmploymentType type = profession.getType();
        ListValue<EmploymentType> employmentType = data.getEmploymentType();
        if (employmentType != null
                    && employmentType.getChoice() != null
                    && type != null
                    && employmentType.getChoice().contains(type)
        ) {
            return 1;
        }
        return 0;
    }

    public int calculateResidents(Prioset prioset, PropertySearcherUserProfileData userProfile) {
        PriosetData data = prioset.getData();
        if (data == null) {
            return 0;
        }

        if (data.getResidents() != null
                && userProfile.getResidents() != null
                && data.getResidents().getLowerBound() <= userProfile.getResidents()
                && data.getResidents().getUpperBound() >= userProfile.getResidents()
        ) {
            return 1;
        }

        return 0;
    }

    public int calculateAnimal(Prioset prioset, PropertySearcherUserProfileData userProfile) {
        AdditionalInformation information = userProfile.getAdditionalInformation();
        if (prioset.getData().getAnimals() == 0 || information == null || information.getAnimals() == null) {
            return 0;
        }
        return information.getAnimals() ? 0 : 1;
    }

    public int calculateChildren(Prioset prioset, PropertySearcherUserProfileData userProfile) {
        HouseholdType householdType = userProfile.getHouseholdType();
        if (prioset.getData().getChildren() == 0 || householdType == null) {
            return 1;
        }
        if (HouseholdType.FAMILY.equals(householdType) || HouseholdType.SINGLE_WITH_CHILDREN.equals(householdType)) {
            return 0;
        }
        return 1;
    }
}
