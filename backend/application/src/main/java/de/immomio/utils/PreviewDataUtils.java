package de.immomio.utils;

import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.appointment.Appointment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.ZoneOffset.UTC;

/**
 * @author Fabian Beck
 */

public class PreviewDataUtils {

    public static final String PREVIEW_PS_EMAIL = "max.mustermann@immomio.de";
    public static final String PREVIEW_PS_NAME = "Mustermann";
    public static final String PREVIEW_PS_FIRSTNAME = "Max";

    public static final String PREVIEW_PROPERTY_NAME = "4 Zimmer in Sonsbeck";
    public static final String PREVIEW_PROPERTY_EXTERNAL_ID = "00123456789";
    public static final double PREVIEW_PROPERTY_BASE_PRICE = 1000.0;
    public static final double PREVIEW_PROPERTY_SIZE = 115.0;
    public static final double PREVIEW_PROPERTY_ROOMS = 4.0;

    public static final String PREVIEW_PROPERTY_IMAGE_URL = "https://immomio-asset-store.s3.eu-central-1.amazonaws.com/dummy_property_title_image.jpg";
    public static final String PREVIEW_PROPERTY_IMAGE_TITLE = "Title Image";
    public static final long PREVIEW_PROPERTY_IMAGE_INDEX = 1L;

    public static final String PREVIEW_PROPERTY_ADDRESS_ZIPCODE = "47665";
    public static final String PREVIEW_PROPERTY_ADDRESS_CITY = "Sonsbeck";
    public static final String PREVIEW_PROPERTY_ADDRESS_STREET = "Hammerbruch";
    public static final String PREVIEW_PROPERTY_ADDRESS_HOUSE_NUMBER = "22";

    public static final Date PREVIEW_PROPERTY_APPOINTMENT_DATE = Date.from(
            LocalDateTime.of(2011, 11, 11, 11, 11).toInstant(UTC));

    public static PropertySearcherUserProfile getPropertySearcherUserProfile() {
        PropertySearcherUser propertySearcherUser = new PropertySearcherUser();
        propertySearcherUser.setEmail(PREVIEW_PS_EMAIL);

        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();
        userProfile.setUser(propertySearcherUser);
        propertySearcherUser.getProfiles().add(userProfile);

        PropertySearcherUserProfileData propertySearcherUserProfileData = new PropertySearcherUserProfileData();
        propertySearcherUserProfileData.setName(PREVIEW_PS_NAME);
        propertySearcherUserProfileData.setFirstname(PREVIEW_PS_FIRSTNAME);
        userProfile.setData(propertySearcherUserProfileData);

        return userProfile;
    }

    public static Property getProperty() {
        Property property = new Property();
        PropertyData propertyData = new PropertyData();
        property.setExternalId(PREVIEW_PROPERTY_EXTERNAL_ID);
        property.setType(PropertyType.FLAT);
        propertyData.setName(PREVIEW_PROPERTY_NAME);
        propertyData.setExternalId(PREVIEW_PROPERTY_EXTERNAL_ID);
        propertyData.setBasePrice(PREVIEW_PROPERTY_BASE_PRICE);
        propertyData.setSize(PREVIEW_PROPERTY_SIZE);
        propertyData.setRooms(PREVIEW_PROPERTY_ROOMS);

        List<S3File> attachments = new ArrayList<>();
        S3File titleImage = new S3File();
        titleImage.setUrl(PREVIEW_PROPERTY_IMAGE_URL);
        titleImage.setTitle(PREVIEW_PROPERTY_IMAGE_TITLE);
        titleImage.setIndex(PREVIEW_PROPERTY_IMAGE_INDEX);
        attachments.add(titleImage);
        propertyData.setAttachments(attachments);

        Address address = new Address();
        address.setZipCode(PREVIEW_PROPERTY_ADDRESS_ZIPCODE);
        address.setCity(PREVIEW_PROPERTY_ADDRESS_CITY);
        address.setStreet(PREVIEW_PROPERTY_ADDRESS_STREET);
        address.setHouseNumber(PREVIEW_PROPERTY_ADDRESS_HOUSE_NUMBER);
        propertyData.setAddress(address);

        property.setData(propertyData);

        ArrayList<Appointment> appointments = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointment.setDate(PREVIEW_PROPERTY_APPOINTMENT_DATE);

        property.setAppointments(appointments);

        return property;
    }
}
