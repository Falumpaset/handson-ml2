package de.immomio.service.propertysearcher;

import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.property.EmploymentType;
import de.immomio.data.propertysearcher.entity.user.MissingProfileField;
import de.immomio.data.propertysearcher.entity.user.ProfileCompletenessResponseBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import lombok.Getter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.immomio.data.base.type.common.FileType.CREDIT_REPORT;
import static de.immomio.data.base.type.common.FileType.INCOME_STATEMENT;
import static de.immomio.data.base.type.common.FileType.WB_CERTIFICATE;
import static de.immomio.data.base.type.property.EmploymentType.HOUSEHOLD_MANAGER;
import static de.immomio.data.base.type.property.EmploymentType.LOOKING_FOR_WORK;
import static de.immomio.data.base.type.property.EmploymentType.RETIRED;
import static de.immomio.data.base.type.property.EmploymentType.STUDENT;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.ADDITIONAL_INFO_ANIMALS;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.ADDITIONAL_INFO_WBS;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.ADDRESS_CITY;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.ADDRESS_HOUSE_NUMBER;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.ADDRESS_STREET;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.ADDRESS_ZIP_CODE;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.DATE_OF_BIRTH;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.DOCUMENT_CREDIT_REPORT;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.DOCUMENT_INCOME_STATEMENT;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.DOCUMENT_WBS;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.HOUSEHOLD_TYPE;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.PROFESSION_INCOME;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.PROFESSION_SUBTYPE;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.PROFESSION_TYPE;
import static de.immomio.data.propertysearcher.entity.user.MissingProfileField.RESIDENTS;

/**
 * @author Maik Kingma
 */

public abstract class AbstractPropertySearcherUserService {

    public ProfileCompletenessResponseBean calculateProfileCompleteness(PropertySearcherUserProfile userProfile) {
        PropertySearcherUserProfileData profileData = userProfile.getData();
        Address address = userProfile.getAddress();
        List<MissingProfileField> missingFields = new ArrayList<>();

        CounterWrapper counter = new CounterWrapper(0, 0);

        // Profession
        getProfessionCompleteness(profileData, missingFields, counter);

        // AdditionalInformation
        getAdditionalInformationCompleteness(profileData, missingFields, counter);

        // General Profile
        getGeneralProfileCompleteness(profileData, missingFields, counter);

        // Address
        getAddressCompleteness(address, missingFields, counter);

        // Attachments
        getAttachmentCompleteness(profileData, missingFields, counter);

        ProfileCompletenessResponseBean profileCompleteness = new ProfileCompletenessResponseBean();
        BigDecimal categoryCount = BigDecimal.valueOf(counter.getCategoryCount());
        double completenessPercentage = BigDecimal.valueOf(counter.getInfoProvided())
                .divide(categoryCount, 2, RoundingMode.HALF_UP)
                .doubleValue();
        profileCompleteness.setCompletenessPercentage(completenessPercentage);
        profileCompleteness.setMissingFields(missingFields);

        return profileCompleteness;
    }

    private void getAttachmentCompleteness(
            PropertySearcherUserProfileData profile,
            List<MissingProfileField> missingFields,
            CounterWrapper counter
    ) {
        counter.increaseCategoryCountBy(2);
        List<S3File> attachments = profile.getAttachments();
        FileType[] fileTypes = {CREDIT_REPORT, INCOME_STATEMENT};

        List<S3File> collect = attachments.stream()
                .filter(attachment -> Arrays.asList(fileTypes).contains(attachment.getType()))
                .collect(Collectors.toList());
        checkAndAddMissingFields(collect, missingFields, DOCUMENT_INCOME_STATEMENT, INCOME_STATEMENT);
        checkAndAddMissingFields(collect, missingFields, DOCUMENT_CREDIT_REPORT, CREDIT_REPORT);

        int size = collect.size();
        int numberOfFileTypes = fileTypes.length;
        int numberOfFiles = size > numberOfFileTypes ? numberOfFileTypes : size;
        counter.increaseInfoProvidedBy(numberOfFiles);

        AdditionalInformation additionalInformation = profile.getAdditionalInformation();
        if (additionalInformation != null && BooleanUtils.isTrue(additionalInformation.getWbs())) {
            counter.increaseCategoryCountBy(1);
            counter.conditionalIncreaseInfoProvided(
                    attachments.stream()
                            .anyMatch(attachment -> attachmentTypeEquals(attachment, WB_CERTIFICATE)),
                    DOCUMENT_WBS,
                    missingFields);
        }
    }

    private boolean attachmentTypeEquals(S3File attachment, FileType type) {
        return attachment.getType().equals(type);
    }

    private void checkAndAddMissingFields(
            List<S3File> collect,
            List<MissingProfileField> missingFields,
            MissingProfileField missingProfileField,
            FileType fileType
    ) {
        if (collect.stream().noneMatch(attachment -> attachmentTypeEquals(attachment, fileType))) {
            missingFields.add(missingProfileField);
        }
    }

    private void getAddressCompleteness(
            Address address,
            List<MissingProfileField> missingFields,
            CounterWrapper counter
    ) {
        if (address != null) {
            counter.increaseCategoryCountBy(4);
            counter.conditionalIncreaseInfoProvided(address.getStreet(), ADDRESS_STREET, missingFields);
            counter.conditionalIncreaseInfoProvided(address.getHouseNumber(), ADDRESS_HOUSE_NUMBER, missingFields);
            counter.conditionalIncreaseInfoProvided(address.getZipCode(), ADDRESS_ZIP_CODE, missingFields);
            counter.conditionalIncreaseInfoProvided(address.getCity(), ADDRESS_CITY, missingFields);
        } else {
            addAllMissingFields(
                    Arrays.asList(ADDRESS_STREET, ADDRESS_HOUSE_NUMBER, ADDRESS_ZIP_CODE, ADDRESS_CITY),
                    missingFields);
        }
    }

    private void addAllMissingFields(List<MissingProfileField> newFields, List<MissingProfileField> current) {
        current.addAll(newFields);
    }

    private void getGeneralProfileCompleteness(
            PropertySearcherUserProfileData profile,
            List<MissingProfileField> missingFields,
            CounterWrapper counter
    ) {
        counter.increaseCategoryCountBy(3);
        counter.conditionalIncreaseInfoProvided(profile.getHouseholdType(), HOUSEHOLD_TYPE, missingFields);
        counter.conditionalIncreaseInfoProvided(profile.getDateOfBirth(), DATE_OF_BIRTH, missingFields);
        counter.conditionalIncreaseInfoProvided(profile.getResidents(), RESIDENTS, missingFields);
    }

    private void getAdditionalInformationCompleteness(
            PropertySearcherUserProfileData profile,
            List<MissingProfileField> missingFields,
            CounterWrapper counter
    ) {
        counter.increaseCategoryCountBy(2);
        AdditionalInformation additionalInformation = profile.getAdditionalInformation();

        if (additionalInformation != null) {
            counter.conditionalIncreaseInfoProvided(
                    additionalInformation.getWbs(),
                    ADDITIONAL_INFO_WBS,
                    missingFields);
            counter.conditionalIncreaseInfoProvided(
                    additionalInformation.getAnimals(),
                    ADDITIONAL_INFO_ANIMALS,
                    missingFields);
        } else {
            addAllMissingFields(Arrays.asList(ADDITIONAL_INFO_WBS, ADDITIONAL_INFO_ANIMALS), missingFields);
        }
    }

    private void getProfessionCompleteness(
            PropertySearcherUserProfileData profile,
            List<MissingProfileField> missingFields,
            CounterWrapper counter
    ) {
        counter.increaseCategoryCountBy(3);
        Profession profession = profile.getProfession();
        if (profession != null) {
            counter.conditionalIncreaseInfoProvided(profession.getType(), PROFESSION_TYPE, missingFields);
            if (isProfessionDescriptionNotRequired(profession)) {
                counter.increaseInfoProvidedBy(2);
            } else {
                counter.conditionalIncreaseInfoProvided(profession.getIncome(), PROFESSION_INCOME, missingFields);
                counter.conditionalIncreaseInfoProvided(profession.getSubType(), PROFESSION_SUBTYPE, missingFields);
            }
        } else {
            addAllMissingFields(Arrays.asList(PROFESSION_INCOME, PROFESSION_TYPE, PROFESSION_SUBTYPE), missingFields);
        }
    }

    private boolean isProfessionDescriptionNotRequired(Profession profession) {
        EmploymentType employmentType = profession.getType();

        return employmentType == LOOKING_FOR_WORK || employmentType == RETIRED
                || employmentType == STUDENT || employmentType == HOUSEHOLD_MANAGER;
    }

    @Getter
    private class CounterWrapper {

        private Integer categoryCount;

        private Integer infoProvided;

        CounterWrapper(Integer categoryCount, Integer infoProvided) {
            this.categoryCount = categoryCount;
            this.infoProvided = infoProvided;
        }

        void increaseCategoryCountBy(Integer i) {
            this.categoryCount = this.categoryCount + i;
        }

        void increaseInfoProvidedBy(Integer i) {
            this.infoProvided = this.infoProvided + i;
        }

        void increaseInfoProvided() {
            this.infoProvided = this.infoProvided + 1;
        }

        void conditionalIncreaseInfoProvided(
                Object condition,
                MissingProfileField fieldName,
                List<MissingProfileField> missingFields
        ) {
            if (Objects.nonNull(condition)) {
                if (condition instanceof String && StringUtils.isBlank(condition.toString())) {
                    missingFields.add(fieldName);
                } else {
                    increaseInfoProvided();
                }
            } else {
                missingFields.add(fieldName);
            }
        }
    }
}
