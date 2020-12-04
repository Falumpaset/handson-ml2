package de.immomio.service.shared;

import de.immomio.cloud.service.url.UrlService;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentBean;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.LandlordMailConfigurator;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Niklas Lindemann
 */

@Component
public class EmailModelProvider {

    private final UrlService urlService;
    private final PropertySearcherMailConfigurator propertySearcherMailConfigurator;
    private final LandlordMailConfigurator landlordMailConfigurator;
    @Value("${timezone.europe}")
    private String ZONE_ID;

    @Autowired
    public EmailModelProvider(UrlService urlService,
            PropertySearcherMailConfigurator propertySearcherMailConfigurator,
            LandlordMailConfigurator landlordMailConfigurator) {
        this.urlService = urlService;
        this.propertySearcherMailConfigurator = propertySearcherMailConfigurator;
        this.landlordMailConfigurator = landlordMailConfigurator;
    }

    public Map<String, Object> createPropertyModel(Property property) {
        Map<String, Object> model = new HashMap<>();

        appendProperty(model, property);

        LandlordCustomer customer = property.getCustomer();
        appendLandlordCustomer(model, customer);

        return model;
    }

    public Map<String, Object> createUserProfilePropertyModel(PropertySearcherUserProfile userProfile, Property property) {
        Map<String, Object> model = createPropertyModel(property);

        appendUserProfile(model, userProfile);

        return model;
    }

    public void appendFormattedDate(Date date, Map<String, Object> data, String key) {
        TimeZone timeZone = TimeZone.getTimeZone(ZONE_ID);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
        dateFormat.setTimeZone(timeZone);
        data.put(key, dateFormat.format(date));
    }

    public void appendAppointment(Map<String, Object> model, Appointment appointment) {
        model.put(ModelParams.MODEL_APPOINTMENT, new AppointmentBean(appointment));
        appendFormattedDate(appointment.getDate(), model, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);
    }

    public void appendProperty(Map<String, Object> model, Property property) {
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property, urlService.getApplicationLink(property.getId())));
    }

    public void appendUserProfile(Map<String, Object> model, PropertySearcherUserProfile userProfile) {
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
    }

    public void appendUser(Map<String, Object> model, LandlordUser user) {
        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_USER_PROFILE, user.getProfile());
    }

    public void appendLandlordCustomer(Map<String, Object> model, LandlordCustomer customer) {
        model.put(ModelParams.MODEL_PREFERENCES, customer.getPreferences());
        appendCustomerBranding(model, customer);
    }

    public Map<String, Object> buildBaseContractMailData(String token, DigitalContractSigner signer) {
        DigitalContract digitalContract = signer.getDigitalContract();
        DigitalContractSignerData signerData = signer.getData();
        Map<String, Object> data = new HashMap<>();
        if (signer.getType() == DigitalContractSignerType.TENANT) {
            appendCustomerBranding(data, signer.getDigitalContract().getCustomer());
        }

        data.put(ModelParams.MODEL_CONTRACT_SIGNER_DATA, signerData);
        data.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, digitalContract.getPropertyData());

        appendToken(data, token);

        return data;
    }

    public void appendReturnUrl(Map<String, Object> dataToExtend, String returnUrl) {
        dataToExtend.put(ModelParams.RETURN_URL, returnUrl);
    }

    public void appendCustomerBranding(Map<String, Object> dataToExtend, LandlordCustomer customer) {
        dataToExtend.put(ModelParams.MODEL_ALLOW_BRANDING, customer.isBrandingAllowed());
        dataToExtend.put(ModelParams.MODEL_BRANDING_LOGO, customer.getBrandingLogo());
    }

    public void appendApplicationId(Map<String, Object> dataToExtend, PropertyApplication application) {
        dataToExtend.put(ModelParams.MODEL_APPLICATION_ID, application.getId());
    }

    public Map<String, Object> createPsMailModel(PropertySearcherUserProfile userProfile, String token) {
        Map<String, Object> model = new HashMap<>();
        appendUserProfile(model, userProfile);
        appendToken(model, token);
        appendReturnUrl(model, propertySearcherMailConfigurator.buildAppUrl());
        return model;
    }

    public Map<String, Object> createModelPsUserAndReturnUrl(PropertySearcherUserProfile userProfile) {
        HashMap<String, Object> model = new HashMap<>();
        appendUserProfile(model, userProfile);
        appendReturnUrl(model, propertySearcherMailConfigurator.buildAppUrl());
        return model;
    }

    public void appendLandlordAppUrl(Map<String, Object> data) {
        data.put(ModelParams.RETURN_URL, landlordMailConfigurator.buildAppUrl());
    }

    public void appendToken(Map<String, Object> model, String token) {
        model.put(ModelParams.MODEL_TOKEN, token);
    }

    public void appendPSAppUrl(Map<String, Object> model) {
        appendReturnUrl(model, propertySearcherMailConfigurator.buildAppUrl());
    }

    public void appendSubjectPlaceholder(Map<String, Object> model, String subject) {
        model.put(ModelParams.SUBJECT_PLACEHOLDER, subject);
    }
}
