package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;
import de.immomio.beans.ChangeEmailBean;
import de.immomio.beans.ResetPasswordBean;
import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.beans.landlord.LandlordCustomerRegisterBean;
import de.immomio.beans.landlord.LandlordRegisterBean;
import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.customer.user.right.Right;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.base.json.JsonPayload;
import de.immomio.data.base.type.addon.AddonType;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.LandlordCustomerSize;
import de.immomio.data.base.type.customer.LandlordCustomerType;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.price.CurrencyType;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.discount.LandlordCustomerProductAddonDiscount;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.permissionscheme.productpermissionscheme.LandlordProductPermissionScheme;
import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.product.LandlordCustomerProduct;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.addon.LandlordAddonProduct;
import de.immomio.data.landlord.entity.product.addonproduct.LandlordCustomerAddonProduct;
import de.immomio.data.landlord.entity.product.productaddon.LandlordProductAddon;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherRight;
import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherUsertypeRight;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelper {

    private static final String DEFAULT_PASSWORD = "password";

    public static String generateEmail() {
        return "test_" + DateTime.now().getMillis() + UUID.randomUUID() + "@immomio.de";
    }

    public static String generateToken() {
        return "test_token" + DateTime.now().getMillis() + UUID.randomUUID();
    }

    public static String generateText(String data) {
        return "test_" + data + "_" + DateTime.now().getMillis();
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
                .mapToObj(i -> generateApplication(status))
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

    public static PropertyApplication generateApplication(ApplicationStatus status) {
        return generateApplication(status, generatePropertySearcherUser("test@mail.de", "Test", "Person", 1L), 1L);
    }

    public static PropertyApplication generateApplication(ApplicationStatus status, PropertySearcherUserProfile userProfile, Long id) {
        LandlordCustomer customer = generateLandlordCustomer(1L);
        Property property = generateProperty(customer, 1L);
        PropertyApplication application = new PropertyApplication();
        application.setProperty(property);
        application.setUserProfile(userProfile);
        application.setStatus(status);
        application.setCreated(new Date());
        setId(application, id);

        return application;
    }

    public static Property generateProperty(LandlordCustomer customer, Long id) {
        Property property = new Property();
        property.setCustomer(customer);
        property.setUser(customer.getResposibleUser());
        setId(property, id);
        PropertyData data = new PropertyData();
        data.setSize(60.0);
        data.setBasePrice(100.0);
        data.setAddress(new Address());
        property.setData(data);

        return property;
    }

    public static Map<String, Object> generatePasswordModelMap() {
        PropertySearcherUserProfile userProfile = generatePropertySearcherUser(999L);
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_TOKEN, "123123");
        model.put(ModelParams.RETURN_URL, "testurl");
        return model;
    }

    public static List<PropertySearcherUsertypeRight> generateUserTypeRights() {
        List<PropertySearcherUsertypeRight> rights = new ArrayList<>();
        rights.add(generatePropertySearcherUserTypeRight());
        rights.add(generatePropertySearcherUserTypeRight());
        rights.add(generatePropertySearcherUserTypeRight());

        return rights;
    }

    public static PropertySearcherUsertypeRight generatePropertySearcherUserTypeRight() {
        PropertySearcherUsertypeRight usertypeRight = new PropertySearcherUsertypeRight();
        PropertySearcherRight propertySearcherRight = new PropertySearcherRight();
        Right right = new Right();
        right.setShortCode("testing123");
        right.setName("test");
        propertySearcherRight.setRight(right);
        usertypeRight.setRight(propertySearcherRight);

        return usertypeRight;
    }

    public static LandlordCustomer generateLandlordCustomerWithAddon(AddonType addonType, Long id) {
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
        setId(customer, id);

        return customer;
    }

    public static LandlordCustomer generateLandlordCustomer(Long id) {
        return generateLandlordCustomerWithAddon(null, id);
    }

    public static LandlordUser generateLandlordUser(LandlordCustomer customer, LandlordUsertype landlordUsertype, Long id) {
        LandlordUser landlordUser = new LandlordUser();
        landlordUser.setEmail("test@mail.de");
        landlordUser.setCustomer(customer);
        landlordUser.setUsertype(landlordUsertype);
        landlordUser.setProfile(new LandlordUserProfile());
        landlordUser.setEnabled(true);
        setId(landlordUser, id);

        return landlordUser;
    }

    public static LandlordUser generateLandlordUser(LandlordUsertype landlordUsertype, Long id) {
        return generateLandlordUser(generateLandlordCustomer(1L), landlordUsertype, 1L);
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
        appointmentAcceptance.setCreated(new Date());

        return appointmentAcceptance;
    }

    public static Appointment generateAppointment() throws Exception {
        return generateAppointment(new Date());
    }

    public static Appointment generateAppointment(Date date) {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setProperty(generateProperty(generateLandlordCustomerWithAddon(AddonType.AGENT, 1L), 1L));
        appointment.setCreated(new Date());
        setId(appointment, 1L);

        return appointment;
    }
    public static Prioset createPrioset(boolean template, Long id) {
        Prioset prioset = new Prioset();
        prioset.setTemplate(template);
        prioset.getProperties().add(generateProperty(generateLandlordCustomer(1L), 1L));
        setId(prioset, id);

        return prioset;
    }

    public static CustomUserDetails generateCustomUserDetails() {
        CustomUserDetails<LandlordUser, LandlordCustomer> userDetails = new CustomUserDetails<>();
        userDetails.setId(1L);
        userDetails.setCustomer(generateLandlordCustomer(1L));

        return userDetails;
    }

    public static Prioset generatePrioset(LandlordCustomer customer, Long id) {
        Prioset prioset = new Prioset();
        prioset.setCustomer(customer);
        setId(prioset, id);
        prioset.setName("Testprioset");
        prioset.setTemplate(false);
        prioset.setData(new PriosetData());

        return prioset;
    }

    public static BaseAuthority createAuthority() {
        return new BaseAuthority();
    }

    public static ChangeEmail generateLandlordChangeEmail(LandlordUser user) {
        ChangeEmail changeEmail = new ChangeEmail();
        changeEmail.setUser(user);
        changeEmail.setEmail(generateEmail());
        changeEmail.setToken(generateToken());

        return changeEmail;
    }

    public static PropertySearcherUserProfile generatePropertySearcherUser(String email,
                                                                    String firstName,
                                                                    String lastName,
                                                                    Long id) {
        PropertySearcherUser user = new PropertySearcherUser();
        user.setEmail(email);
        user.setCustomer(generatePropertySearcherCustomer());

        PropertySearcherUserProfile userProfile = new PropertySearcherUserProfile();
        userProfile.setUser(user);
        user.getProfiles().add(userProfile);

        PropertySearcherUserProfileData profile = new PropertySearcherUserProfileData();
        profile.setName(lastName);
        profile.setFirstname(firstName);
        setId(userProfile, id);

        return userProfile;
    }

    public static PropertySearcherUserProfile generatePropertySearcherUser(Long id) {
        PropertySearcherUserProfile userProfile = generatePropertySearcherUser(generateEmail(), generateText("firstName"),
                generateText("lastName"), id);
        userProfile.getUser().setCustomer(new PropertySearcherCustomer());

        return userProfile;
    }

    public static PropertySearcherCustomer generatePropertySearcherCustomer() {
        PropertySearcherCustomer customer = new PropertySearcherCustomer();
        customer.setName("firstName");

        return customer;
    }

    public static Address generateAddress() {
        Address address = new Address();
        address.setCity("Hamburg");
        address.setStreet("Teststreet");
        address.setHouseNumber("2");
        address.setZipCode("33445");
        address.setCountry(CountryCode.DE);

        return address;
    }

    public static PropertyPortal generatePropertyPortal(Portal portal, Long id) {
        PropertyPortal propertyPortal = new PropertyPortal();
        propertyPortal.setPortal(portal);
        setId(propertyPortal, id);

        return propertyPortal;
    }

    public static CustomQuestionCreateBean createCustomQuestionBeaFromJsonFile(String path, Class classParam) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(getResourceStream(path, classParam), CustomQuestionCreateBean.class);
    }

    private static InputStream getResourceStream(String path, Class classParam) {
        return classParam.getResourceAsStream(path);
    }

    public static CustomQuestion createCustomQuestion(CustomQuestionCreateBean bean, LandlordCustomer customer) {
        CustomQuestion question = new CustomQuestion();
        question.setCustomer(customer);
        question.setDesiredResponses(bean.getDesiredResponses());
        JsonPayload jsonPayload = bean.getJsonPayload();
        question.setForm(jsonPayload.getForm());
        question.setSchema(jsonPayload.getSchema());
        question.setScoring(bean.isScoring());

        return question;
    }

    public static LandlordCustomerProductAddonDiscount createLandlordCustomerProductAddonDiscount(
            Long id,
            LandlordDiscount discount,
            LandlordProductAddon productAddon
    ) {
        LandlordCustomerProductAddonDiscount addonDiscount = new LandlordCustomerProductAddonDiscount();
        addonDiscount.setDiscount(discount);
        addonDiscount.setProductAddon(productAddon);
        if (id != null) {
            setId(addonDiscount, id);
        }

        return addonDiscount;
    }

    public static LandlordDiscount createLandlordDiscount(Double value, Date endDate, Long id) {
        LandlordDiscount landlordDiscount = new LandlordDiscount();
        landlordDiscount.setEndDate(endDate);
        landlordDiscount.setValue(value);
        setId(landlordDiscount, id);

        return landlordDiscount;
    }

    public static LandlordProductAddon createProductAddon(Long id) {
        LandlordProductAddon landlordProductAddon = new LandlordProductAddon();
        setId(landlordProductAddon, id);

        return landlordProductAddon;
    }

    public static S3File createS3File(String filename) {
        S3File s3File = new S3File();
        s3File.setName(filename);
        s3File.setTitle(filename);
        s3File.setIdentifier(UUID.randomUUID().toString());
        return s3File;
    }

}
