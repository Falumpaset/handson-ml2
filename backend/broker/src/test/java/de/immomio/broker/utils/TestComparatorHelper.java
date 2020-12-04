package de.immomio.broker.utils;

import de.immomio.data.base.entity.customer.user.right.Right;
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
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.landlord.entity.user.right.LandlordRight;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.GeoCoordinates;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.email.MessageBean;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;

import java.util.Comparator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestComparatorHelper {

    public static void compareLandlordUsers(LandlordUser first, LandlordUser second) {
        assertEquals(first.getUsertype(), second.getUsertype());

        compareLandlordProfile(first.getProfile(), second.getProfile());
        compareLandlordCustomer(first.getCustomer(), second.getCustomer());
    }

    public static void compareLandlordProfile(LandlordUserProfile first, LandlordUserProfile second) {
        assertEquals(first.getFirstname(), second.getFirstname());
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getGender(), second.getGender());
        assertEquals(first.getPhone(), second.getPhone());
        assertEquals(first.getTitle(), second.getTitle());
    }

    public static void compareLandlordCustomer(LandlordCustomer first, LandlordCustomer second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
        assertEquals(first.getCustomerType(), second.getCustomerType());
        assertEquals(first.getInvoiceEmail(), second.getInvoiceEmail());
        assertEquals(first.getManagementUnits(), second.getManagementUnits());
        assertEquals(first.getSubDomain(), second.getSubDomain());
        assertEquals(first.getTaxId(), second.getTaxId());
        assertEquals(first.getLocation(), second.getLocation());
        assertEquals(first.getPriceMultiplier(), second.getPriceMultiplier());
        assertEquals(first.getCustomerSize(), second.getCustomerSize());
    }

    public static void compareLandlordDiscount(LandlordDiscount first, LandlordDiscount second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getValue(), second.getValue());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
    }

    public static void comparePropertyProposal(PropertyProposal first, PropertyProposal second) {
        assertEquals(first.getState(), second.getState());
        assertEquals(first.getProperty(), second.getProperty());
        assertEquals(first.getUserProfile(), second.getUserProfile());
    }

    public static void compareLandlordCoupon(LandlordCoupon first, LandlordCoupon second) {
        assertNotNull(first.getDiscount());
        assertNotNull(first.getCouponUsages());

        assertNotNull(second.getDiscount());
        assertNotNull(second.getCouponUsages());

        assertEquals(first.getCouponUsages().size(), second.getCouponUsages().size());
        compareLandlordDiscount(first.getDiscount(), second.getDiscount());

        Comparator<LandlordCouponUsage> comparator = Comparator.comparing(LandlordCouponUsage::getCreated)
                .thenComparing(LandlordCouponUsage::getDescription);

        first.getCouponUsages().sort(comparator);
        second.getCouponUsages().sort(comparator);
        for (int index = 0; index < first.getCouponUsages().size(); index++) {
            compareLandlordCouponUsage(first.getCouponUsages().get(index), second.getCouponUsages().get(index));
        }
    }

    public static void compareLandlordCouponUsage(LandlordCouponUsage first, LandlordCouponUsage second) {
        assertEquals(first.getCreated(), second.getCreated());
        assertEquals(first.getDescription(), second.getDescription());
    }

    public static void compareLandlordPrice(LandlordPrice first, LandlordPrice second) {
        assertEquals(first.getFixedPart(), second.getFixedPart(), 0.0001);
        assertEquals(first.getVariablePart(), second.getVariablePart(), 0.0001);
        assertEquals(first.getCurrency(), second.getCurrency());
    }

    public static void compareLandlordProduct(LandlordProduct first, LandlordProduct second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
        assertEquals(first.getSubscriptionPeriod(), second.getSubscriptionPeriod());
    }

    public static void compareLandlordProductPrice(LandlordProductPrice first, LandlordProductPrice second) {
        assertEquals(first.getPrice(), second.getPrice());
        assertEquals(first.getLocation(), second.getLocation());
        assertArrayEquals(first.getPaymentMethods(), second.getPaymentMethods());
    }

    public static void compareLandlordEmail(LandlordEmail first, LandlordEmail second) {
        assertEquals(first.getTemplate(), second.getTemplate());
        compareLandlordUsers(first.getUser(), second.getUser());
        compareLandlordCustomer(first.getCustomer(), second.getCustomer());
    }

    public static void compareMessageBean(MessageBean first, MessageBean second) {
        assertEquals(first.getFromEmail(), second.getFromEmail());
        assertEquals(first.getToEmail(), second.getToEmail());
        assertEquals(first.getToCc(), second.getToCc());
        assertEquals(first.getSubject(), second.getSubject());
        assertEquals(first.getMessageBody(), second.getMessageBody());
        assertEquals(first.getToBcc(), second.getToBcc());
    }

    public static void compareCredentials(Credential first, Credential second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getCreated(), second.getCreated());
        assertEquals(first.getUpdated(), second.getUpdated());
        assertEquals(first.getEncrypted(), second.getEncrypted());
        assertEquals(first.getPortal(), second.getPortal());
    }

    public static void compareLandlordPermissionScheme(LandlordPermissionScheme first,
                                                       LandlordPermissionScheme second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
    }

    public static void compareLandlordPermissionSchemeRight(LandlordPermissionSchemeRight first,
                                                            LandlordPermissionSchemeRight second) {
        assertEquals(first.getUserType(), second.getUserType());
        compareLandlordRight(first.getRight(), second.getRight());
    }

    public static void compareLandlordRight(LandlordRight first, LandlordRight second) {
        compareRight(first.getRight(), second.getRight());
    }

    public static void compareRight(Right first, Right second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
        assertEquals(first.getGroup(), second.getGroup());
        assertEquals(first.getShortCode(), second.getShortCode());
    }

    public static void compareS3File(S3File first, S3File second) {
        assertEquals(first.getExtension(), second.getExtension());
        assertEquals(first.getFilename(), second.getFilename());
        assertEquals(first.getIdentifier(), second.getIdentifier());
        assertEquals(first.getIndex(), second.getIndex());
        assertEquals(first.getType(), second.getType());
        assertEquals(first.getUrl(), second.getUrl());
        assertEquals(first.getName(), second.getName());
    }

    public static void comparePrioset(Prioset first, Prioset second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getProperties(), second.getProperties());

        assertTrue(first.getProperties().containsAll(second.getProperties()));
        assertEquals(first.getProperties().size(), second.getProperties().size());
        compareLandlordCustomer(first.getCustomer(), second.getCustomer());
    }

    public static void compareInvoice(LandlordInvoice first, LandlordInvoice second) {
        assertEquals(first.getInvoiceId(), second.getInvoiceId());
        assertEquals(first.getStatus(), second.getStatus());
        assertEquals(first.getInvoiceDate(), second.getInvoiceDate());
        assertEquals(first.getPrice(), second.getPrice());
        assertEquals(first.getPostDiscountPrice(), second.getPostDiscountPrice());
        assertEquals(first.getCurrency(), second.getCurrency());
        assertEquals(first.getTaxRate(), second.getTaxRate());
        assertEquals(first.isCancellation(), second.isCancellation());
        assertEquals(first.getPaymentMethod(), second.getPaymentMethod());

        compareLandlordCustomer(first.getCustomer(), second.getCustomer());
    }

    public static void compareProperty(Property first, Property second){
        assertEquals(first.getId(), second.getId());
        assertEquals(first.getData(), second.getData());
    }

    public static void compareAddress(Address first, Address second) {
        assertEquals(first.getCity(), second.getCity());
        assertEquals(first.getStreet(), second.getStreet());
        assertEquals(first.getHouseNumber(), second.getHouseNumber());
        assertEquals(first.getCountry(), second.getCountry());

        compareGeoCoordinates(first.getCoordinates(), second.getCoordinates());
    }

    public static void compareGeoCoordinates(GeoCoordinates first, GeoCoordinates second) {
        assertEquals(first.getLatitude(), second.getLatitude());
        assertEquals(first.getLongitude(), second.getLongitude());
    }

    public static void comparePublishLogs(PublishLog first, PublishLog second) {
        assertEquals(first.getId(), second.getId());
        compareProperty(first.getProperty(), second.getProperty());
    }

}
