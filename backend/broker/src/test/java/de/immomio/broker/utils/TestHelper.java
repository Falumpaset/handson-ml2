package de.immomio.broker.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import de.immomio.beans.ChangeEmailBean;
import de.immomio.beans.ResetPasswordBean;
import de.immomio.beans.landlord.LandlordCustomerRegisterBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.constants.GenderType;
import de.immomio.constants.product.ProductSubscriptionPeriod;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.customer.user.right.Right;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.credential.CredentialProperty;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.LandlordCustomerSize;
import de.immomio.data.base.type.customer.LandlordCustomerType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.invoice.InvoiceStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.price.CurrencyType;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.coupon.LandlordCoupon;
import de.immomio.data.landlord.entity.couponusage.LandlordCouponUsage;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.email.LandlordEmail;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.permissionscheme.LandlordPermissionScheme;
import de.immomio.data.landlord.entity.permissionscheme.permissionschemeright.LandlordPermissionSchemeRight;
import de.immomio.data.landlord.entity.permissionscheme.productpermissionscheme.LandlordProductPermissionScheme;
import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.productprice.LandlordProductPrice;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.property.publish.PublishState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.landlord.entity.user.right.LandlordRight;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.discount.PropertySearcherDiscount;
import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductPrice;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherProspectOptIn;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.SearchProfileData;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.email.MessageBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.neovisionaries.i18n.CountryCode.DE;

/**
 * @author Freddy Sawma.
 */
public class TestHelper {

    private static final String DEFAULT_PASSWORD = "password";

    public static PropertySearcherUserProfile generatePropertySearcherUser() {
        return new PropertySearcherUserProfile();
    }

    public static PropertySearcherUserProfile generatePropertySearcherUser(String email, PropertySearcherCustomer customer) {
        PropertySearcherUser propertySearcherUser = new PropertySearcherUser();
        propertySearcherUser.setEmail(email);
        propertySearcherUser.setCustomer(customer);
        propertySearcherUser.setType(PropertySearcherUserType.UNREGISTERED);
        propertySearcherUser.setProspectOptIn(generatePropertySearcherProspectOptIn(propertySearcherUser));

        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();

        userProfile.setUser(propertySearcherUser);
        userProfile.setData(generatePropertySearcherUserProfile());

        return userProfile;
    }

    public static PropertySearcherProspectOptIn generatePropertySearcherProspectOptIn(PropertySearcherUser user) {
        PropertySearcherProspectOptIn prospectOptIn = new PropertySearcherProspectOptIn();
        prospectOptIn.setUser(user);
        prospectOptIn.setOptInForProspect(false);
        prospectOptIn.setCreated(new Date());
        prospectOptIn.setUpdated(new Date());

        return prospectOptIn;
    }

    public static PropertySearcherUserProfile generatePropertySearcherUser(PropertySearcherCustomer customer) {
        return generatePropertySearcherUser(generateEmail(), customer);
    }

    public static PropertySearcherUserProfileData generatePropertySearcherUserProfile() {
        PropertySearcherUserProfileData propertySearcherUserProfile = new PropertySearcherUserProfileData();
        propertySearcherUserProfile.setGender(GenderType.FEMALE);
        propertySearcherUserProfile.setName(generateName());
        propertySearcherUserProfile.setFirstname(generateName());

        return propertySearcherUserProfile;
    }

    public static PropertySearcherSearchProfile generatePropertySearcherSearchProfile() {
        PropertySearcherSearchProfile propertySearcherSearchProfile = new PropertySearcherSearchProfile();
        propertySearcherSearchProfile.setUserProfile(generatePropertySearcherUser());
        propertySearcherSearchProfile.setManuallyCreated(true);
        propertySearcherSearchProfile.setProperty(generateProperty(generateLandlordCustomer(), 32173621L));
        SearchProfileData data = new SearchProfileData();
        data.setAddress(TestHelper.generateAddress());
        propertySearcherSearchProfile.setData(data);

        return propertySearcherSearchProfile;
    }

    public static List<PropertySearcherSearchProfile> generatePropertySearcherSearchProfileList(int numberOfSearchProfiles) {
        List<PropertySearcherSearchProfile> propertySearcherSearchProfiles = new ArrayList<>();
        for (int i = 0; i < numberOfSearchProfiles; i++) {
            propertySearcherSearchProfiles.add(generatePropertySearcherSearchProfile());
        }
        return propertySearcherSearchProfiles;
    }

    public static PropertyProposal generatePropertyProposal() {
        PropertyProposal propertyProposal = new PropertyProposal();
        propertyProposal.setProperty(generateProperty(generateLandlordCustomer()));
        propertyProposal.setSearchProfile(generatePropertySearcherSearchProfile());
        propertyProposal.setUserProfile(generatePropertySearcherUser());
        setId(propertyProposal, 1L);

        return propertyProposal;
    }

    public static PropertySearcherCustomer generateCustomer() {
        PropertySearcherCustomer propertySearcherCustomer = new PropertySearcherCustomer();
        propertySearcherCustomer.setLocation(CustomerLocation.DE);
        propertySearcherCustomer.setPaymentMethods(
                new ArrayList<>(Arrays.asList(new PaymentMethod(PaymentMethodType.PAYPAL, true))));
        propertySearcherCustomer.setName(generateName());
        propertySearcherCustomer.setPaymentDetails(new HashMap<>());

        return propertySearcherCustomer;
    }

    public static Address generateAddress() {
        Address address = new Address();
        address.setCity("Hamburg");
        address.setCountry(DE);
        GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLongitude(53.547280);
        coordinates.setLatitude(9.995275);
        address.setCoordinates(coordinates);

        return address;
    }

    public static PropertySearcherDiscount generatePropertySearcherDiscount() {
        PropertySearcherDiscount discount = new PropertySearcherDiscount();
        discount.setName(generateName());
        discount.setStartDate(new Date());
        discount.setEndDate(new Date());
        discount.setValue(0.5);

        return discount;
    }

    public static Point generateRandomPoint() {
        GeometryFactory gf = new GeometryFactory();
        Random r = new Random();

        return gf.createPoint(new Coordinate(r.nextDouble(), r.nextDouble()));
    }

    public static PropertySearcherPrice generatePropertySearcherPrice() {
        PropertySearcherPrice price = new PropertySearcherPrice();
        price.setFixedPart(0.5);
        price.setVariablePart(0.4);
        price.setCurrency(CurrencyType.EUR);

        return price;
    }

    public static PropertySearcherProduct generatePropertySearcherProduct() {
        PropertySearcherProduct propertySearcherProduct = new PropertySearcherProduct();
        propertySearcherProduct.setName(generateName());
        propertySearcherProduct.setDescription(generateDescription());
        propertySearcherProduct.setSubscriptionPeriod(ProductSubscriptionPeriod.WEEKLY);

        List<PropertySearcherProductPrice> prices = new ArrayList<>();
        prices.add(generatePropertySearcherProductPrice());
        propertySearcherProduct.setPrices(prices);

        List<PropertySearcherDiscount> discounts = new ArrayList<>();
        discounts.add(generatePropertySearcherDiscount());
        propertySearcherProduct.setDiscounts(discounts);

        return propertySearcherProduct;
    }

    public static PropertySearcherProductPrice generatePropertySearcherProductPrice() {
        PropertySearcherProductPrice propertySearcherProductPrice = new PropertySearcherProductPrice();
        propertySearcherProductPrice.setPrice(generatePropertySearcherPrice());
        propertySearcherProductPrice.setLocation(CustomerLocation.DE);
        propertySearcherProductPrice.setPaymentMethods(PaymentMethodType.values());

        return propertySearcherProductPrice;
    }

    public static PropertySearcherProductPrice generatePropertySearcherProductPrice(
            PropertySearcherPrice propertySearcherPrice) {
        PropertySearcherProductPrice propertySearcherProductPrice = new PropertySearcherProductPrice();
        propertySearcherProductPrice.setPrice(propertySearcherPrice);
        propertySearcherProductPrice.setLocation(CustomerLocation.DE);
        propertySearcherProductPrice.setPaymentMethods(PaymentMethodType.values());

        return propertySearcherProductPrice;
    }

    public static LandlordRegisterBean createLandlordRegisterBean(String email) {
        LandlordRegisterBean data = new LandlordRegisterBean();
        data.setEmail(email);
        data.setFirstName("firstName");
        data.setLastName("lastName");
        data.setPassword(DEFAULT_PASSWORD);
        data.setConfirmPassword(DEFAULT_PASSWORD);
        data.setCustomer(createCustomerRegistrationBean("customer 1"));

        return data;
    }

    public static LandlordCustomerRegisterBean createCustomerRegistrationBean(String name) {
        LandlordCustomerRegisterBean data = new LandlordCustomerRegisterBean();
        data.setName(name);
        data.setCustomerSize(LandlordCustomerSize.MEDIUM);
        data.setCustomerType(LandlordCustomerType.OTHER);
        data.setLocation(CustomerLocation.DE);

        return data;
    }

    public static ChangeEmailBean createChangeEmailBean(String email) {
        return new ChangeEmailBean(email);
    }

    public static ResetPasswordBean createResetPasswordBean(String email) {
        return new ResetPasswordBean(email);
    }

    public static List<PropertyApplication> generateApplications(Integer amount, ApplicationStatus status) {
        List<PropertyApplication> applications = IntStream.range(0, amount)
                .mapToObj(i -> generateApplication(status, null))
                .collect(Collectors.toList());

        return applications;
    }

    public static void setId(AbstractEntity entity, Long id) {
        try {
            Field field = null;

            Class clazz = entity.getClass();
            while (field == null) {
                try {
                    field = clazz.getDeclaredField("id");
                } catch (Exception ignored) {

                }
                clazz = clazz.getSuperclass();
            }

            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception ignored) {
        }
    }

    public static PropertyApplication generateApplication(ApplicationStatus status, Portal portal) {
        PropertySearcherUser propertySearcherUser = new PropertySearcherUser();
        propertySearcherUser.setEmail("test@mail.de");
        setId(propertySearcherUser, 1L);

        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();
        userProfile.setUser(propertySearcherUser);

        LandlordCustomer customer = generateLandlordCustomer();
        Property property = generateProperty(customer, 1L);

        PropertyApplication application = new PropertyApplication(portal);

        application.setProperty(property);
        application.setUserProfile(userProfile);
        application.setStatus(status);

        return application;
    }

    public static Property generateProperty(LandlordCustomer customer, Long id) {
        Property property = new Property();
        property.setCustomer(customer);
        property.setUser(customer.getResposibleUser());
        setId(property, id);
        PropertyData data = new PropertyData();
        data.setAddress(new Address());
        property.setData(data);

        return property;
    }

    public static LandlordCustomer generateLandlordCustomerWithAddon(AddonType addonType) {
        LandlordCustomer customer = new LandlordCustomer();
        LandlordCustomerProduct product = new LandlordCustomerProduct();
        LandlordCustomerAddonProduct addonProduct = new LandlordCustomerAddonProduct();
        LandlordAddonProduct landlordAddonProduct = new LandlordAddonProduct();
        LandlordProduct landlordProduct = new LandlordProduct();

        product.setProduct(landlordProduct);
        landlordProduct.getPermissionSchemes().add(new LandlordProductPermissionScheme());
        landlordAddonProduct.setAddonType(addonType);
        addonProduct.setAddonProduct(landlordAddonProduct);
        product.getAddons().add(addonProduct);

        customer.getProducts().add(product);
        customer.setCustomerType(LandlordCustomerType.PROPERTYMANAGEMENT);

        return customer;
    }

    public static LandlordCustomer generateLandlordCustomer() {
        return generateLandlordCustomerWithAddon(null);
    }

    public static LandlordUser generateLandlordUser(LandlordCustomer customer, LandlordUsertype landlordUsertype) {
        LandlordUser landlordUser = new LandlordUser();
        landlordUser.setEmail("test@mail.de");
        landlordUser.setCustomer(customer);
        landlordUser.setUsertype(landlordUsertype);
        landlordUser.setProfile(new LandlordUserProfile());
        setId(landlordUser, ThreadLocalRandom.current().nextLong(1000000));

        return landlordUser;
    }

    public static LandlordUser generateLandlordUser(LandlordUsertype landlordUsertype) {
        return generateLandlordUser(generateLandlordCustomer(), landlordUsertype);
    }

    public static LandlordPrice generateLandlordPrice(double price) {
        LandlordPrice landlordPrice = new LandlordPrice();
        landlordPrice.setCurrency(CurrencyType.EUR);
        landlordPrice.setFixedPart(price);
        landlordPrice.setVariablePart(0.0);

        return landlordPrice;
    }

    public static AppointmentAcceptance generateAppointmentAcceptance(Appointment appointment, PropertyApplication application, AppointmentAcceptanceState state) {
        AppointmentAcceptance appointmentAcceptance = new AppointmentAcceptance();
        appointmentAcceptance.setApplication(application);
        appointmentAcceptance.setAppointment(appointment);
        appointmentAcceptance.setState(state);

        return appointmentAcceptance;
    }

    public static Appointment generateAppointment() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setDate(new Date());
        appointment.setProperty(generateProperty(generateLandlordCustomerWithAddon(AddonType.AGENT), 1L));
        setId(appointment, 1L);

        return appointment;

    }

    public static Prioset createPrioset(boolean template) {
        Prioset prioset = new Prioset();
        prioset.setTemplate(template);
        prioset.getProperties().add(generateProperty(generateLandlordCustomer(), 1L));

        return prioset;
    }

    public static PublishLog generatePublishLog(Property property) {
        List<Portal> portals = property.getPortals().stream().map(PropertyPortal::getPortal).collect(Collectors.toList());
        PublishLog publishLog = new PublishLog();
        publishLog.setCustomer(property.getCustomer());
        publishLog.setPortals(portals);
        publishLog.setPropertyTask(property.getTask());
        publishLog.setProperty(property);
        publishLog.setAgentInfo(new AgentInfo(property.getUser()));
        publishLog.setPublishState(PublishState.SUCCESS);

        return publishLog;
    }

    public static CustomUserDetails generateCustomUserDetails() {
        CustomUserDetails<LandlordUser, LandlordCustomer> userDetails = new CustomUserDetails<>();
        userDetails.setId(1L);
        userDetails.setCustomer(generateLandlordCustomer());
        return userDetails;
    }

    public static BaseAuthority createAuthority() {
        return new BaseAuthority();
    }

    public static LandlordUser generateLandlordUser() {
        return new LandlordUser();
    }

    public static LandlordUser generateLandlordUser(String email, LandlordCustomer customer) {
        LandlordUser landlordUser = new LandlordUser();
        landlordUser.setUsertype(LandlordUsertype.EMPLOYEE);
        landlordUser.setEmail(email);
        landlordUser.setProfile(generateLandlordUserProfile());
        landlordUser.setCustomer(customer);

        return landlordUser;
    }

    public static LandlordUser generateLandlordUser(LandlordCustomer customer) {
        return generateLandlordUser(generateEmail(), customer);
    }

    public static LandlordUserProfile generateLandlordUserProfile() {
        LandlordUserProfile landlordUserProfile = new LandlordUserProfile();
        landlordUserProfile.setGender(GenderType.MALE);
        landlordUserProfile.setName(generateName());
        landlordUserProfile.setFirstname(generateName());

        return landlordUserProfile;
    }

    public static LandlordCoupon generateLandlordCoupon(LandlordDiscount landlordDiscount) {
        LandlordCoupon landlordCoupon = new LandlordCoupon();
        landlordCoupon.setCreated(new Date());
        landlordCoupon.setUpdated(new Date());
        landlordCoupon.setCouponCode("random-valid-code");
        landlordCoupon.setOccurence(1);
        landlordCoupon.setDiscount(landlordDiscount);

        return landlordCoupon;
    }

    public static LandlordCouponUsage generateLandlordCouponUsage(final LandlordUser landlordUser) {
        LandlordCouponUsage landlordCouponUsage = new LandlordCouponUsage();
        landlordCouponUsage.setCreated(new Date());
        landlordCouponUsage.setDescription("used it 'cause I could");
        landlordCouponUsage.setUser(landlordUser);

        return landlordCouponUsage;
    }

    public static LandlordDiscount generateLandlordDiscount() {
        LandlordDiscount landlordDiscount = new LandlordDiscount();
        landlordDiscount.setName(generateName());
        landlordDiscount.setStartDate(new Date());
        landlordDiscount.setEndDate(new Date());
        landlordDiscount.setValue(0.5);

        return landlordDiscount;
    }

    public static LandlordPrice generateLandlordPrice() {
        LandlordPrice price = new LandlordPrice();
        price.setFixedPart(0.5);
        price.setVariablePart(0.4);
        price.setCurrency(CurrencyType.EUR);

        return price;
    }

    public static LandlordProduct generateLandlordProduct() {
        LandlordProduct landlordProduct = new LandlordProduct();
        landlordProduct.setName(generateName());
        landlordProduct.setDescription(generateDescription());
        landlordProduct.setSubscriptionPeriod(ProductSubscriptionPeriod.WEEKLY);

        List<LandlordProductPrice> prices = new ArrayList<>();
        prices.add(generateLandlordProductPrice());
        landlordProduct.setPrices(prices);

        List<LandlordDiscount> discounts = new ArrayList<>();
        discounts.add(generateLandlordDiscount());
        landlordProduct.setDiscounts(discounts);

        return landlordProduct;
    }

    public static LandlordProductPrice generateLandlordProductPrice() {
        LandlordProductPrice landlordProductPrice = new LandlordProductPrice();
        landlordProductPrice.setPrice(generateLandlordPrice());
        landlordProductPrice.setLocation(CustomerLocation.DE);
        landlordProductPrice.setPaymentMethods(PaymentMethodType.values());

        return landlordProductPrice;
    }

    public static LandlordProductPrice generateLandlordProductPrice(LandlordPrice landlordPrice) {
        LandlordProductPrice landlordProductPrice = new LandlordProductPrice();
        landlordProductPrice.setPrice(landlordPrice);
        landlordProductPrice.setLocation(CustomerLocation.DE);
        landlordProductPrice.setPaymentMethods(PaymentMethodType.values());

        return landlordProductPrice;
    }

    public static LandlordProductPrice generateLandlordProductPrice(LandlordPrice landlordPrice,
                                                                    LandlordCustomer customer) {
        LandlordProductPrice landlordProductPrice = generateLandlordProductPrice(landlordPrice);
        landlordProductPrice.setCustomer(customer);

        return landlordProductPrice;
    }

    public static LandlordEmail generateLandlordEmail(LandlordUser user, LandlordCustomer landlordCustomer) {
        LandlordEmail landlordEmail = new LandlordEmail();
        landlordEmail.setTemplate(MailTemplate.NEW_EMAIL);

        landlordEmail.setUser(user);
        landlordEmail.setCustomer(landlordCustomer);

        return landlordEmail;
    }

    public static Credential generateCredential(LandlordCustomer landlordCustomer) {
        Credential credential = new Credential();
        credential.setCreated(new Date());
        credential.setUpdated(new Date());
        credential.setEncrypted(false);
        credential.setName("credential-name");
        credential.setPortal(Portal.IMMOBILIENSCOUT24_DE);
        Map<String, String> properties = new HashMap<>();
        properties.put(CredentialProperty.USERNAME.name(), "username");
        credential.setProperties(properties);
        credential.setCustomer(landlordCustomer);

        return credential;
    }

    public static MessageBean generateMessageBean() {
        MessageBean messageBean = new MessageBean();
        messageBean.setFromEmail(generateEmail());
        messageBean.setToCc(generateEmail());
        messageBean.setToEmail(generateEmail());
        messageBean.setMessageBody(generateText("message body"));
        messageBean.setSubject(generateText("subject"));

        return messageBean;
    }

    public static LandlordPermissionScheme generateLandlordPermissionScheme() {
        LandlordPermissionScheme landlordPermissionScheme = new LandlordPermissionScheme();
        landlordPermissionScheme.setName(generateName());
        landlordPermissionScheme.setDescription(generateDescription());

        List<LandlordPermissionSchemeRight> landlordPermissionSchemeRights = new ArrayList<>();
        landlordPermissionSchemeRights.add(generateLandlordPermissionSchemeRight());
        landlordPermissionScheme.setRights(landlordPermissionSchemeRights);

        return landlordPermissionScheme;
    }

    public static LandlordPermissionSchemeRight generateLandlordPermissionSchemeRight() {
        LandlordPermissionSchemeRight landlordPermissionSchemeRight = new LandlordPermissionSchemeRight();

        landlordPermissionSchemeRight.setRight(generateLandlordRight());
        landlordPermissionSchemeRight.setUserType(LandlordUsertype.EMPLOYEE);

        return landlordPermissionSchemeRight;
    }

    public static LandlordRight generateLandlordRight() {
        LandlordRight landlordRight = new LandlordRight();
        landlordRight.setRight(generateRight());

        return landlordRight;
    }

    public static Right generateRight() {
        Right right = new Right();
        right.setName(generateName());
        right.setDescription(generateDescription());
        right.setGroup(generateText("group"));
        right.setShortCode(generateText("short-code"));

        return right;
    }

    public static LandlordMailBrokerContainer createLandlordMailBrokerContainer() {
        LandlordMailBrokerContainer container = new LandlordMailBrokerContainer();
        container.setUserId(1337L);

        return container;
    }

    public static PropertySearcherProfileMailBrokerContainer createPropertySearcherMailBrokerContainer() {
        PropertySearcherProfileMailBrokerContainer container =
                new PropertySearcherProfileMailBrokerContainer();
        container.setUserProfileId(2232L);
        container.setCustomerId(32183L);
        container.setEmail("test@immomio.de");
        container.setSubject("testing");
        container.setTemplate(MailTemplate.APPLICATION_ACCEPTED_V1);
        container.setData(new HashMap<>());

        return container;
    }

    public static Prioset generatePrioset(LandlordCustomer customer) {
        Prioset prioset = new Prioset();
        prioset.setTemplate(true);
        prioset.setLocked(false);
        prioset.setCustomer(customer);
        prioset.setData(new PriosetData());
        prioset.setName("Testprioset");

        return prioset;
    }

    public static Property generateProperty(LandlordCustomer landlordCustomer) {
        PropertyData data = new PropertyData();
        data.setName("Testobjekt");
        Property property = new Property();
        property.setTask(PropertyTask.IDLE);
        property.setCustomer(landlordCustomer);
        property.setData(data);
        property.getData().setAddress(generateAddress());
        property.setPortals(generatePropertyPortals(property));

        return property;
    }

    public static S3File generateS3File(String identifier) {
        S3File s3File = new S3File();
        s3File.setIdentifier(identifier);
        s3File.setUrl(identifier);

        return s3File;
    }

    public static List<PropertyPortal> generatePropertyPortals(Property property) {
        PropertyPortal portal = new PropertyPortal();
        portal.setPortal(Portal.IMMOBILIENSCOUT24_DE);
        portal.setState(PropertyPortalState.ACTIVE);
        portal.setProperty(property);
        ArrayList<PropertyPortal> portals = new ArrayList<>();
        portals.add(portal);
        property.setPortals(portals);

        return portals;
    }

    public static LandlordInvoice generateInvoice(LandlordCustomer customer, Long invoiceId) {
        LandlordInvoice invoice = new LandlordInvoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setStatus(InvoiceStatus.PROCESSING);
        invoice.setInvoiceDate(new Date());
        invoice.setPrice(250.0);
        invoice.setPostDiscountPrice(200.0);
        invoice.setCurrency(CurrencyType.EUR);
        invoice.setTaxRate(40.0);
        invoice.setCancellation(false);
        invoice.setPaymentMethod(PaymentMethodType.INVOICE);
        invoice.setCustomer(customer);

        return invoice;
    }

    public static PropertyTenant generatePropertyTenant(Property property, PropertySearcherUserProfile userProfile) {
        PropertyTenant propertyTenant = new PropertyTenant();
        propertyTenant.setProperty(property);
        propertyTenant.setUserProfile(userProfile);
        propertyTenant.setCreated(new Date());

        return propertyTenant;
    }

    public static String generateEmail() {
        return "test_" + DateTime.now().getMillis() + UUID.randomUUID() + "@immomio.de";
    }

    public static String generateName() {
        return generateText("name");
    }

    public static String generateDescription() {
        return generateText("description");
    }

    public static String generateText(String data) {
        return "test_" + data + "_" + DateTime.now().getMillis();
    }
}
