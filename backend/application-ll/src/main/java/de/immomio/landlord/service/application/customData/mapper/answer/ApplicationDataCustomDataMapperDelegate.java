package de.immomio.landlord.service.application.customData.mapper.answer;

import de.immomio.data.base.type.application.ApplicationCustomDataAnswerType;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldAnswerBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.bean.customer.settings.DkLevelCustomerSettings;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevelConfig;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.profile.details.AdditionalInformation;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.BOOLEAN;
import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.DATE;
import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.ENUM;
import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.FILE;
import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.NUMBER;
import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.STRING;
import static de.immomio.data.landlord.bean.property.dk.DkApprovalLevelConfig.DK1;
import static de.immomio.data.landlord.bean.property.dk.DkApprovalLevelConfig.DK2;

@Component
public class ApplicationDataCustomDataMapperDelegate extends ApplicationCustomDataMapperDelegate {

    private static final String HIDING_STRING = "***";

    @Autowired
    public ApplicationDataCustomDataMapperDelegate() {
        super(ApplicationCustomDataFieldType.DATA);
    }

    @PostConstruct
    public void postConstruct() {
        addField(createField("score", false, NUMBER, DkLevelCustomerSettings::getScoringLevel), (field, application) -> createAnswer(application.getScore()));
        addField(createField("portrait", true, FILE, DkLevelCustomerSettings::getPortraitLevel),
                (field, application) -> createAnswer(getData(application).getPortrait()));
        addField(createField("first_name", true, STRING, DK1), (field, application) -> createAnswer(getFirstName(application)));
        addField(createField("last_name", true, STRING, DK1), (field, application) -> createAnswer(getLastName(application)));
        addField(createField("birthday", true, DATE, DK2), (field, application) -> createAnswer(getData(application).getDateOfBirth()));
        addField(createField("income", false, NUMBER, DK2), (field, application) -> createAnswer(
                getProfession(application).getIncome() != null && getProfession(application).getIncome() > 0 ? getProfession(application).getIncome() : null));
        addField(createField("profession_type", false, ENUM, DK2), (field, application) -> createAnswer(getProfession(application).getType()));
        addField(createField("profession_sub_type", false, STRING, DK2), (field, application) -> createAnswer(getProfession(application).getSubType()));
        addField(createField("wbs", false, BOOLEAN, DK1), (field, application) -> createAnswer(getAdditionalInformation(application).getWbs()));
        addField(createField("animals", false, BOOLEAN, DK2), (field, application) -> createAnswer(getAdditionalInformation(application).getAnimals()));
        addField(createField("bailment", false, BOOLEAN, DK2), (field, application) -> createAnswer(getAdditionalInformation(application).getBailment()));
        addField(createField("household_type", false, ENUM, DK2), (field, application) -> createAnswer(getData(application).getHouseholdType()));
        addField(createField("household_residents", false, ENUM, DK2), (field, application) -> createAnswer(getData(application).getResidents()));
        addField(createField("email", true, STRING, DK1), (field, application) -> createAnswer(application.getUserProfile().getEmail()));
        addField(createField("phone", true, STRING, DK1), (field, application) -> createAnswer(getData(application).getPhone()));
        addField(createField("address", true, STRING, DK1), (field, application) -> addressMapper(application));
    }

    @Override
    public List<ApplicationCustomDataFieldBaseBean> getPossibleFields(Property property) {
        return new ArrayList<>(mapperDelegates.keySet());
    }

    private ApplicationCustomDataFieldBean createField(String fieldType,
            boolean anonymisable,
            ApplicationCustomDataAnswerType answerType, Function<DkLevelCustomerSettings, DkApprovalLevelConfig> dkLevelCalculation) {
        return new ApplicationCustomDataFieldBean(ApplicationCustomDataFieldType.DATA,
                CUSTOM_DATA + fieldType,
                fieldType,
                null,
                null,
                anonymisable,
                answerType,
                dkLevelCalculation);
    }

    private ApplicationCustomDataFieldBean createField(String fieldType,
            boolean anonymisable,
            ApplicationCustomDataAnswerType answerType,
            DkApprovalLevelConfig level) {
        return createField(fieldType, anonymisable, answerType, dkLevelCustomerSettings -> level);
    }

    private Profession getProfession(PropertyApplication application) {
        Profession profession = getData(application).getProfession();
        return profession != null ? profession : new Profession();
    }

    private AdditionalInformation getAdditionalInformation(PropertyApplication application) {
        AdditionalInformation additionalInformation = getData(application).getAdditionalInformation();
        return additionalInformation != null ? additionalInformation : new AdditionalInformation();
    }

    private PropertySearcherUserProfileData getData(PropertyApplication application) {
        PropertySearcherUserProfileData userProfileData = application.getUserProfile().getData();
        return userProfileData != null ? userProfileData : new PropertySearcherUserProfileData();
    }

    private ApplicationCustomDataFieldAnswerBean addressMapper(PropertyApplication application) {
        Address address = application.getUserProfile().getAddress();
        return createAnswer(address != null ? address.toString() : null);
    }

    private String getFirstName(PropertyApplication application) {
        String firstname = getData(application).getFirstname();
        return isApplicationDkLevelTooLow(application) ? firstname : firstname.charAt(0) + HIDING_STRING;
    }

    private String getLastName(PropertyApplication application) {
        String mame = getData(application).getName();
        return isApplicationDkLevelTooLow(application) ? mame : mame.charAt(0) + HIDING_STRING;
    }

    private boolean isApplicationDkLevelTooLow(PropertyApplication application) {
        return !getNameLevel(application).isHigherThan(application.getDkApprovalLevel());
    }

    private DkApprovalLevelConfig getNameLevel(PropertyApplication application) {
        return application.getProperty().getCustomer().getCustomerSettings().getDkLevelCustomerSettings().getNameLevel();
    }
}
