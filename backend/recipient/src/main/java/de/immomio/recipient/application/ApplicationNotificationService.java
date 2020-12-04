package de.immomio.recipient.application;

import de.immomio.cloud.service.url.UrlService;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.email.ListingDetail;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationNotificationService {

    private final UrlService urlService;

    private final PropertySearcherMailSender propertySearcherMailSender;

    @Autowired
    public ApplicationNotificationService(UrlService urlService, PropertySearcherMailSender propertySearcherMailSender) {
        this.urlService = urlService;
        this.propertySearcherMailSender = propertySearcherMailSender;
    }

    public void sendApplicationEmail(boolean newUser, PropertySearcherUserProfile userProfile, Property property, ListingDetail listingDetails) {
        LandlordCustomer landlordCustomer = property.getCustomer();

        PropertySearcherUserProfileEmailBean userBean = new PropertySearcherUserProfileEmailBean(userProfile);
        userBean.setEmail(URLEncoder.encode(userBean.getEmail(), StandardCharsets.UTF_8));

        Map<String, Object> data = new HashMap<>();
        data.put(ModelParams.MODEL_FLAT, createPropertyBean(property));
        data.put(ModelParams.MODEL_PREFERENCES, landlordCustomer.getPreferences());
        data.put(ModelParams.MODEL_BRANDING_LOGO, landlordCustomer.getBrandingLogo());
        data.put(ModelParams.MODEL_ALLOW_BRANDING, landlordCustomer.isBrandingAllowed());
        data.put(ModelParams.MODEL_PORTAL, listingDetails.getListingPortal());
        data.put(ModelParams.MODEL_USER, userBean);
        data.put("listingDetails", listingDetails);

        propertySearcherMailSender.send(userProfile,
                getMailTemplate(property.getType(), newUser),
                getMailSubject(property.getType(), newUser),
                new Object[]{property.getData().getName()},
                data,
                landlordCustomer.getId());
    }

    private MailTemplate getMailTemplate(PropertyType propertyType, boolean newUser) {
        if (propertyType == PropertyType.GARAGE || propertyType == PropertyType.COMMERCIAL) {
            return MailTemplate.APPLICATION_NEW_OBJECT_TYPE;
        }

        if (!newUser) {
            return MailTemplate.APPLICATION_KNOWN;
        }

        return MailTemplate.APPLICATION_NEW;
    }

    private String getMailSubject(PropertyType propertyType, boolean newUser) {
        if (propertyType == PropertyType.GARAGE || propertyType == PropertyType.COMMERCIAL) {
            return "application.new.object.type.subject";
        }

        if (!newUser) {
            return "application.known.subject";
        }

        return "application.new.subject";
    }

    private PropertyMailBean createPropertyBean(Property property) {
        String applicationLink = urlService.getApplicationLink(property.getId());
        return new PropertyMailBean(property, applicationLink);
    }
}
