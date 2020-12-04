package de.immomio.service.propertySearcher;

import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.property.EmploymentType;
import de.immomio.data.base.type.property.HouseholdType;
import de.immomio.data.base.type.property.PersonalStatus;
import de.immomio.data.propertysearcher.entity.user.MissingProfileField;
import de.immomio.data.propertysearcher.entity.user.ProfileCompletenessResponseBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import de.immomio.data.propertysearcher.entity.user.profile.details.Law;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.propertysearcher.entity.user.profile.details.Smoking;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.service.propertySearcher.PropertySearcherUserService;
import de.immomio.utils.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PropertySearcherUserServiceTest{

    private static final String DD_MM_YY = "dd/MM/yy";
    private static final String BIRTHDATE_1 = "02/12/90";
    private static final double DELTA = 0.000000000001;
    private static final double COMPLETENESS_PERCENTAGE_PARTIAL_PROFILE = 0.3;
    private static final double COMPLETENESS_PERCENTAGE_EMPTY_PROFILE = 0.0;
    private static final double COMPLETENESS_PERCENTAGE_COMPLETE_PROFILE = 1.0;
    private static final String JANUARY_2018 = "January 2018";
    private static final String IT = "IT";

    @InjectMocks
    PropertySearcherUserService userService;

    @Test
    public void emptyProfileTest() {
        //Empty Profile test prep
        PropertySearcherUserProfile userWithEmptyProfile = getPropertySearcherUser(2L).getMainProfile();

        ProfileCompletenessResponseBean completenessResponseEmptyExpected = new ProfileCompletenessResponseBean();
        completenessResponseEmptyExpected.setCompletenessPercentage(COMPLETENESS_PERCENTAGE_EMPTY_PROFILE);
        completenessResponseEmptyExpected.setMissingFields(
                Arrays.asList(
                        MissingProfileField.PROFESSION_INCOME,
                        MissingProfileField.PROFESSION_TYPE,
                        MissingProfileField.PROFESSION_SUBTYPE,
                        MissingProfileField.ADDITIONAL_INFO_WBS,
                        MissingProfileField.ADDITIONAL_INFO_ANIMALS,
                        MissingProfileField.HOUSEHOLD_TYPE,
                        MissingProfileField.DATE_OF_BIRTH,
                        MissingProfileField.RESIDENTS,
                        MissingProfileField.ADDRESS_STREET,
                        MissingProfileField.ADDRESS_HOUSE_NUMBER,
                        MissingProfileField.ADDRESS_ZIP_CODE,
                        MissingProfileField.ADDRESS_CITY,
                        MissingProfileField.DOCUMENT_INCOME_STATEMENT,
                        MissingProfileField.DOCUMENT_CREDIT_REPORT
                ));
        ProfileCompletenessResponseBean completenessResponseEmptyActual =
                userService.calculateProfileCompleteness(userWithEmptyProfile);

        // Testing of empty profile completeness
        Assertions.assertEquals(
                completenessResponseEmptyExpected.getCompletenessPercentage(),
                completenessResponseEmptyActual.getCompletenessPercentage(),
                DELTA,
                "Empty Profile completeness percentage should be: ");

        Assertions.assertEquals(
                completenessResponseEmptyExpected.getMissingFields(),
                completenessResponseEmptyActual.getMissingFields(),
                "Empty Profile missing fields should be: ");
    }

    @Test
    public void completeProfileTest() {
        // test prep complete Profile
        PropertySearcherUserProfile userWithCompleteProfile = getPropertySearcherUserCompleteProfile();
        ProfileCompletenessResponseBean completenessResponseCompleteExpected = new ProfileCompletenessResponseBean();
        completenessResponseCompleteExpected.setCompletenessPercentage(COMPLETENESS_PERCENTAGE_COMPLETE_PROFILE);
        completenessResponseCompleteExpected.setMissingFields(new ArrayList<>());
        ProfileCompletenessResponseBean completenessResponseCompleteActual =
                userService.calculateProfileCompleteness(userWithCompleteProfile);

        // test complete profile
        Assertions.assertEquals(
                completenessResponseCompleteExpected.getMissingFields(),
                completenessResponseCompleteActual.getMissingFields(),
                "Empty Profile missing fields should be: ");
        Assertions.assertEquals(
                completenessResponseCompleteExpected.getCompletenessPercentage(),
                completenessResponseCompleteActual.getCompletenessPercentage(),
                DELTA,
                "Empty Profile completeness percentage should be: ");
    }

    @Test
    public void profileCompletenessWithEmptyCity() {
        PropertySearcherUserProfile userWithCompleteProfile = getPropertySearcherUserCompleteProfile();
        ProfileCompletenessResponseBean profileCompletenessExpected = new ProfileCompletenessResponseBean();
        profileCompletenessExpected.setCompletenessPercentage(COMPLETENESS_PERCENTAGE_COMPLETE_PROFILE);
        userWithCompleteProfile.setAddress(TestHelper.generateAddress());
        userWithCompleteProfile.getAddress().setCity(" ");
        ProfileCompletenessResponseBean profileCompletenessActual =
                userService.calculateProfileCompleteness(userWithCompleteProfile);

        Assertions.assertNotEquals(
                profileCompletenessActual.getCompletenessPercentage(),
                profileCompletenessExpected.getCompletenessPercentage(),
                DELTA,
                "Partial profile completeness percentage should be: ");
    }

    @Test
    public void partialProfileTest() {
        // Partial Profile Test Prep
        PropertySearcherUserProfile userWithPartialProfile = getPropertySearcherUserPartialProfile();
        ProfileCompletenessResponseBean completenessResponsePartialExpected = new ProfileCompletenessResponseBean();
        completenessResponsePartialExpected.setCompletenessPercentage(COMPLETENESS_PERCENTAGE_PARTIAL_PROFILE);
        completenessResponsePartialExpected.setMissingFields(
                Arrays.asList(
                        MissingProfileField.PROFESSION_INCOME,
                        MissingProfileField.PROFESSION_TYPE,
                        MissingProfileField.PROFESSION_SUBTYPE,
                        MissingProfileField.HOUSEHOLD_TYPE,
                        MissingProfileField.RESIDENTS,
                        MissingProfileField.ADDRESS_STREET,
                        MissingProfileField.ADDRESS_HOUSE_NUMBER,
                        MissingProfileField.ADDRESS_ZIP_CODE,
                        MissingProfileField.ADDRESS_CITY,
                        MissingProfileField.DOCUMENT_INCOME_STATEMENT,
                        MissingProfileField.DOCUMENT_CREDIT_REPORT
                ));
        ProfileCompletenessResponseBean completenessResponsePartialActual =
                userService.calculateProfileCompleteness(userWithPartialProfile);

        // Testing of partial profile completeness
        Assertions.assertEquals(
                completenessResponsePartialActual.getCompletenessPercentage(),
                completenessResponsePartialExpected.getCompletenessPercentage(),
                DELTA,
                "Partial Profile completeness percentage should be: ");

        Assertions.assertEquals(
                completenessResponsePartialActual.getMissingFields(),
                completenessResponsePartialExpected.getMissingFields(),
                "Partial Profile missing fields should be: ");
    }

    @Test
    public void lookingForWorkTest() {
        PropertySearcherUserProfile userWithCompleteProfile = getPropertySearcherLookingForWorkProfile();
        ProfileCompletenessResponseBean completenessResponseCompleteExpected = new ProfileCompletenessResponseBean();
        completenessResponseCompleteExpected.setCompletenessPercentage(COMPLETENESS_PERCENTAGE_COMPLETE_PROFILE);
        completenessResponseCompleteExpected.setMissingFields(new ArrayList<>());
        ProfileCompletenessResponseBean completenessResponseCompleteActual =
                userService.calculateProfileCompleteness(userWithCompleteProfile);

        Assertions.assertEquals(
                completenessResponseCompleteActual.getMissingFields(),
                completenessResponseCompleteExpected.getMissingFields(),
                "Empty Profile missing fields should be: ");
        Assertions.assertEquals(
                completenessResponseCompleteExpected.getCompletenessPercentage(),
                completenessResponseCompleteActual.getCompletenessPercentage(),
                DELTA,
                "Empty Profile completeness percentage should be: ");
    }
    private PropertySearcherUser getPropertySearcherUser(long id) {
        return TestHelper.generatePropertySearcherUser(TestHelper.generateCustomer());
    }

    private PropertySearcherUserProfile getPropertySearcherUserPartialProfile() {
        PropertySearcherUserProfile userWithPartialProfile = getPropertySearcherUser(1L).getMainProfile();
        PropertySearcherUserProfileData partialProfile = new PropertySearcherUserProfileData();
        DateFormat formatter = new SimpleDateFormat(DD_MM_YY);
        Date date;
        try {
            date = formatter.parse(BIRTHDATE_1);
            partialProfile.setDateOfBirth(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        partialProfile.setMoveInDate(new Date());
        AdditionalInformation additionalInformation = getAdditionalInformation();
        partialProfile.setAdditionalInformation(additionalInformation);
        userWithPartialProfile.setData(partialProfile);
        return userWithPartialProfile;
    }

    private PropertySearcherUserProfile getPropertySearcherUserCompleteProfile() {
        PropertySearcherUserProfile userWithFullProfile = getPropertySearcherUser(3L).getMainProfile();

        PropertySearcherUserProfileData completeProfile = new PropertySearcherUserProfileData();
        DateFormat formatter = new SimpleDateFormat(DD_MM_YY);
        Date date;
        try {
            date = formatter.parse(BIRTHDATE_1);
            completeProfile.setDateOfBirth(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        completeProfile.setMoveInDate(new Date());
        completeProfile.setHouseholdType(HouseholdType.FAMILY);
        completeProfile.setResidents(3);
        completeProfile.setGuarantorExist(true);
        completeProfile.setSmoker(getSmoking());
        completeProfile.setProfession(getProfession());

        completeProfile.setAttachments(generatePSAttachmentsList());
        completeProfile.setLaw(getLaw());
        completeProfile.setPersonalStatus(PersonalStatus.MARRIED);

        AdditionalInformation additionalInformation = getAdditionalInformation();
        completeProfile.setAdditionalInformation(additionalInformation);
        completeProfile.setPortrait(new S3File());

        userWithFullProfile.setData(completeProfile);
        userWithFullProfile.setAddress(TestHelper.generateAddress());

        return userWithFullProfile;
    }

    private PropertySearcherUserProfile getPropertySearcherLookingForWorkProfile() {
        PropertySearcherUserProfile userWithFullProfile = getPropertySearcherUserCompleteProfile();
        userWithFullProfile.getData().setProfession(getLookingForWorkProfession());

        return userWithFullProfile;
    }

    private AdditionalInformation getAdditionalInformation() {
        AdditionalInformation additionalInformation = new AdditionalInformation();
        additionalInformation.setWbs(false);
        additionalInformation.setAnimals(false);
        additionalInformation.setBailment(true);
        return additionalInformation;
    }

    private Law getLaw() {
        Law law = new Law();
        law.setAllowSchufa(true);
        law.setInformationTrueAndComplete(true);
        law.setNoPoliceRecord(true);
        law.setNoRentArrears(true);
        law.setNoTenancyLawConflicts(true);
        return law;
    }

    private Profession getProfession() {
        Profession profession = new Profession();
        profession.setIncome(5000.00);
        profession.setType(EmploymentType.EMPLOYED_UNLIMITED);
        profession.setSubType(IT);
        profession.setEmploymentDate(JANUARY_2018);
        return profession;
    }

    private Profession getLookingForWorkProfession() {
        Profession profession = new Profession();
        profession.setType(EmploymentType.LOOKING_FOR_WORK);
        return profession;
    }

    private Smoking getSmoking() {
        Smoking smoking = new Smoking();
        smoking.setInhouse(false);
        smoking.setSmoker(false);
        return smoking;
    }

    private List<S3File> generatePSAttachmentsList() {
        List<S3File> attachments = new ArrayList<>();

        S3File incomeStatement = new S3File();
        incomeStatement.setType(FileType.INCOME_STATEMENT);

        S3File creditReport = new S3File();
        creditReport.setType(FileType.CREDIT_REPORT);

        attachments.add(incomeStatement);
        attachments.add(creditReport);

        return attachments;
    }

}
