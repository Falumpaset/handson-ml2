package de.immomio.utils;

import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.discount.PropertySearcherDiscount;
import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductPrice;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.email.EmailLog;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestComparatorHelper {

    public static void comparePropertySearcherUsers(PropertySearcherUser first, PropertySearcherUser second) {
        assertEquals(first.getType(), second.getType());

        comparePropertySearcherProfile(first.getMainProfile().getData(), second.getMainProfile().getData());
        comparePropertySearcherCustomer(first.getCustomer(), second.getCustomer());
    }

    public static void comparePropertySearcherProfile(PropertySearcherUserProfileData first,
                                                      PropertySearcherUserProfileData second) {
        assertEquals(first.getFirstname(), second.getFirstname());
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getGender(), second.getGender());
        assertEquals(first.getPhone(), second.getPhone());
        assertEquals(first.getTitle(), second.getTitle());
    }

    public static void comparePropertySearcherCustomer(PropertySearcherCustomer first,
                                                       PropertySearcherCustomer second) {
        assertEquals(first.getInvoiceEmail(), second.getInvoiceEmail());
        assertEquals(first.getLocation(), second.getLocation());
    }

    public static void comparePropertySearcherDiscount(PropertySearcherDiscount first,
                                                       PropertySearcherDiscount second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getValue(), second.getValue());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
    }

    public static void comparePropertySearcherPrice(PropertySearcherPrice first, PropertySearcherPrice second) {
        assertEquals(first.getFixedPart(), second.getFixedPart(), 0.0001);
        assertEquals(first.getVariablePart(), second.getVariablePart(), 0.0001);
        assertEquals(first.getCurrency(), second.getCurrency());
    }

    public static void comparePropertySearcherProduct(PropertySearcherProduct first, PropertySearcherProduct second) {
        assertEquals(first.getName(), second.getName());
        assertEquals(first.getDescription(), second.getDescription());
        assertEquals(first.getSubscriptionPeriod(), second.getSubscriptionPeriod());
    }

    public static void comparePropertySearcherProductPrice(PropertySearcherProductPrice first,
                                                           PropertySearcherProductPrice second) {
        assertEquals(first.getPrice(), second.getPrice());
        assertEquals(first.getLocation(), second.getLocation());
        assertArrayEquals(first.getPaymentMethods(), second.getPaymentMethods());
    }

    public static void compareEmailLogs(EmailLog first, EmailLog second) {
        assertEquals(first.getUser(), second.getUser());
        assertEquals(first.getEventType(), second.getEventType());
    }
}
