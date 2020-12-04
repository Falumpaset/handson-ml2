package de.immomio.utils;

import de.immomio.constants.GenderType;
import de.immomio.constants.product.ProductSubscriptionPeriod;
import de.immomio.data.base.bean.customer.PaymentMethod;
import de.immomio.data.base.type.customer.CustomerLocation;
import de.immomio.data.base.type.customer.PaymentMethodType;
import de.immomio.data.base.type.price.CurrencyType;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.discount.PropertySearcherDiscount;
import de.immomio.data.propertysearcher.entity.price.PropertySearcherPrice;
import de.immomio.data.propertysearcher.entity.product.PropertySearcherProduct;
import de.immomio.data.propertysearcher.entity.product.price.PropertySearcherProductPrice;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TestHelper {

    public static PropertySearcherUserProfile generatePropertySearcherUser() {
        return new PropertySearcherUserProfile();
    }

    public static PropertySearcherUserProfileData generatePropertySearcherUserProfile() {
        PropertySearcherUserProfileData propertySearcherUserProfile = new PropertySearcherUserProfileData();
        propertySearcherUserProfile.setGender(GenderType.FEMALE);
        propertySearcherUserProfile.setName(generateName());
        propertySearcherUserProfile.setFirstname(generateName());
        return propertySearcherUserProfile;
    }

    public static PropertySearcherCustomer generateCustomer() {
        PropertySearcherCustomer propertySearcherCustomer = new PropertySearcherCustomer();
        propertySearcherCustomer.setLocation(CustomerLocation.DE);
        propertySearcherCustomer.setPaymentMethods(
                new ArrayList<PaymentMethod>(Arrays.asList(new PaymentMethod(PaymentMethodType.PAYPAL, true))));
        propertySearcherCustomer.setName(generateName());
        propertySearcherCustomer.setPaymentDetails(new HashMap<>());

        return propertySearcherCustomer;
    }

    public static PropertySearcherDiscount generatePropertySearcherDiscount() {
        PropertySearcherDiscount discount = new PropertySearcherDiscount();
        discount.setName(generateName());
        discount.setStartDate(new Date());
        discount.setEndDate(new Date());
        discount.setValue(0.5);
        return discount;
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
