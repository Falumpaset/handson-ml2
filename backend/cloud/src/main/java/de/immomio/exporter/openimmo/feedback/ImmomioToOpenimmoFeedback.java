package de.immomio.exporter.openimmo.feedback;

import de.immomio.constants.GenderType;
import de.immomio.data.base.type.property.EmploymentType;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.APPRENTICE;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.BIRTHDAY;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.CIVIL_CERVANT;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.EMPLOYED_LIMITED;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.EMPLOYED_UNLIMITED;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.HOUSEHOLD_MANAGER;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.INCOME;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.JOB;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.MR;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.MRS;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.RETIRED;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.SELF_EMPLOYED;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.START_OF_CONTRACT;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.STUDENT;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.S_S;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.TYPE_OF_INCOME;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.UNEMPLOYED;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.WBS_CERTIFICATE;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.WOHNBAU_ID;
import static de.immomio.exporter.openimmo.feedback.ImmoBlueConstants.YYYY_MM_DD;

@Service
public class ImmomioToOpenimmoFeedback {

    private SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);

    public OpenimmoFeedback convert(PropertyApplication propertyApplication) {
        return convert(propertyApplication, null);
    }

    public OpenimmoFeedback convert(PropertyApplication propertyApplication, Date contractStart) {
        return convert(propertyApplication.getProperty(), propertyApplication.getUserProfile(), contractStart);
    }

    public OpenimmoFeedback convert(PropertyTenant propertyTenant) {
        return convert(propertyTenant.getProperty(),
                propertyTenant.getUserProfile(),
                propertyTenant.getContractStart());
    }

    public OpenimmoFeedback convert(Property property, PropertySearcherUserProfile userProfile) {
        return convert(property, userProfile, null);
    }

    private OpenimmoFeedback convert(Property property, PropertySearcherUserProfile userProfile, Date contractStart) {
        OpenimmoFeedback openimmoFeedback = new OpenimmoFeedback();
        OpenimmoObject object = new OpenimmoObject();

        PropertySearcherUserProfileData userProfileData = userProfile.getData();
        PropertyData propertyData = property.getData();

        if (property.getCustomer().getId() == WOHNBAU_ID) {
            object.setPortalUniqueId(propertyData.getReferenceId());
            object.setPortalObjectId(property.getExternalId());
        } else {
            object.setPortalUniqueId(property.getExternalId());
            object.setPortalObjectId(propertyData.getReferenceId());
        }

        OpenimmoInterest openimmoInterest = new OpenimmoInterest();
        openimmoInterest.setIntId(userProfile.getId().toString());
        openimmoInterest.setSalutation(convertSalutation(userProfileData.getGender()));
        openimmoInterest.setFirsName(userProfileData.getFirstname());
        openimmoInterest.setLastName(userProfileData.getName());
        openimmoInterest.setTelephone(userProfileData.getPhone());
        openimmoInterest.setEmail(userProfile.getEmail());

        populateAddress(openimmoInterest, userProfile.getAddress());

        openimmoInterest.setUserDefinedFields(generateDefinedFields(userProfileData, contractStart));
        object.setInterest(openimmoInterest);
        openimmoFeedback.setObject(object);

        return openimmoFeedback;
    }

    private List<OpenimmoUserDefinedField> generateDefinedFields(PropertySearcherUserProfileData userProfile,
                                                                 Date contractStart) {
        List<OpenimmoUserDefinedField> definedFields = new ArrayList<>();

        if (userProfile.getDateOfBirth() != null) {
            definedFields.add(new OpenimmoUserDefinedField(BIRTHDAY, sdf.format(userProfile.getDateOfBirth())));
        }

        if (userProfile.getProfession() != null) {
            definedFields.add(
                    new OpenimmoUserDefinedField(INCOME, String.valueOf(userProfile.getProfession().getIncome())));

            if (userProfile.getProfession().getType() != null) {
                definedFields.add(new OpenimmoUserDefinedField(TYPE_OF_INCOME,
                        convertProfessionTypeToEinkommensart(userProfile.getProfession().getType())));
            }

            if (userProfile.getProfession().getSubType() != null) {
                definedFields.add(new OpenimmoUserDefinedField(JOB, userProfile.getProfession().getSubType()));
            }
        }

        if (userProfile.getAdditionalInformation() != null) {
            definedFields.add(new OpenimmoUserDefinedField(WBS_CERTIFICATE,
                    userProfile.getAdditionalInformation().getWbs().toString()));
        }

        if (contractStart != null) {
            definedFields.add(new OpenimmoUserDefinedField(START_OF_CONTRACT, sdf.format(contractStart)));
        }

        return definedFields;
    }

    private void populateAddress(OpenimmoInterest openimmoInterest, Address address) {
        if (address != null) {
            if (StringUtils.hasText(address.getStreet()) && StringUtils.hasText(address.getHouseNumber())) {
                String streetWithNumber = String.format(S_S, address.getStreet(), address.getHouseNumber());
                openimmoInterest.setStreet(streetWithNumber);
            } else {
                openimmoInterest.setStreet(address.getStreet());
            }

            openimmoInterest.setPostCode(address.getZipCode());
            openimmoInterest.setRegion(address.getCity());
        }
    }

    private static String convertSalutation(GenderType genderType) {
        if (genderType == null) {
            return null;
        }

        switch (genderType) {
            case FEMALE:
                return MRS;
            case MALE:
                return MR;
            default:
                return null;
        }
    }

    private static String convertProfessionTypeToEinkommensart(EmploymentType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case SELF_EMPLOYED:
                return SELF_EMPLOYED;
            case EMPLOYED_UNLIMITED:
                return EMPLOYED_UNLIMITED;
            case EMPLOYED_LIMITED:
                return EMPLOYED_LIMITED;
            case STUDENT:
                return STUDENT;
            case LOOKING_FOR_WORK:
                return UNEMPLOYED;
            case RETIRED:
                return RETIRED;
            case HOUSEHOLD_MANAGER:
                return HOUSEHOLD_MANAGER;
            case APPRENTICE:
                return APPRENTICE;
            case CIVIL_SERVANT:
                return CIVIL_CERVANT;
            default:
                return null;
        }
    }
}
