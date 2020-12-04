package de.immomio.utils;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;

/**
 * @author Niklas Lindemann
 */
public class TenantUtils {

    public static boolean isUserInOtherPrivateTenantPool(PropertySearcherUserProfile userProfile, LandlordCustomer customer) {
        return userProfile.getTenantPoolCustomer() != null && !userProfile.getTenantPoolCustomer().equals(customer);
    }

    public static boolean isUserInPrivateTenantPool(PropertySearcherUserProfile userProfile, LandlordCustomer customer) {
        return userProfile.getTenantPoolCustomer() != null && userProfile.getTenantPoolCustomer().equals(customer);
    }
}
