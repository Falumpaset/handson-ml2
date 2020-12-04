package de.immomio.utils;

import com.neovisionaries.i18n.CountryCode;
import de.immomio.constants.GenderType;
import de.immomio.constants.product.ProductSubscriptionPeriod;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.entity.customer.user.right.Right;
import de.immomio.data.base.type.credential.CredentialProperty;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.LandlordCustomerSize;
import de.immomio.data.base.type.customer.LandlordCustomerType;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.invoice.InvoiceStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.price.CurrencyType;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.coupon.LandlordCoupon;
import de.immomio.data.landlord.entity.couponusage.LandlordCouponUsage;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.discount.LandlordDiscount;
import de.immomio.data.landlord.entity.email.LandlordEmail;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.permissionscheme.LandlordPermissionScheme;
import de.immomio.data.landlord.entity.permissionscheme.permissionschemeright.LandlordPermissionSchemeRight;
import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.product.productprice.LandlordProductPrice;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.landlord.entity.user.right.LandlordRight;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.email.MessageBean;
import de.immomio.mail.sender.templates.MailTemplate;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestHelper {

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

    public static LandlordCustomer generateCustomer() {
        LandlordCustomer landlordCustomer = new LandlordCustomer();
        landlordCustomer.setCustomerType(LandlordCustomerType.PRIVATE);
        landlordCustomer.setInvoiceEmail(generateEmail());
        landlordCustomer.setPreferences(new HashMap<>());
        landlordCustomer.setPriceMultiplier(10.0);
        landlordCustomer.setLocation(CustomerLocation.DE);
        landlordCustomer.setCustomerSize(LandlordCustomerSize.LARGE);
        landlordCustomer.setPaymentMethods(
                new ArrayList<>(Collections.singletonList(
                        new PaymentMethod(PaymentMethodType.INVOICE, true))));
        return landlordCustomer;
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
        landlordEmail.setCreated(new Date());

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

    public static Prioset generatePrioset(LandlordCustomer customer){
        Prioset prioset = new Prioset();
        prioset.setTemplate(true);
        prioset.setLocked(false);
        prioset.setCustomer(customer);
        prioset.setData(new PriosetData());
        prioset.setName("Testprioset");
        return prioset;
    }

    public static Property generateProperty(LandlordCustomer landlordCustomer){
        PropertyData  data = new PropertyData ();
        data.setName("Testobjekt");
        Property property = new Property();
        property.setTask(PropertyTask.IDLE);
        property.setCustomer(landlordCustomer);
        property.setData(PropertyData.builder().build());
        return property;
    }

    public static S3File generateS3File(String identifier){
        S3File s3File = new S3File();
        s3File.setIdentifier(identifier);
        s3File.setUrl(identifier);
        return s3File;
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

    public static Address generateAddress() {
        Address address = new Address();
        address.setCity("Hamburg");
        address.setStreet("Grimm");
        address.setHouseNumber("1234");
        address.setCountry(CountryCode.DE);
        address.setCoordinates(generateGeoCoordinates());

        return address;
    }

    public static GeoCoordinates generateGeoCoordinates() {
        GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLongitude(48.2345);
        coordinates.setLatitude(56.432);

        return coordinates;
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
