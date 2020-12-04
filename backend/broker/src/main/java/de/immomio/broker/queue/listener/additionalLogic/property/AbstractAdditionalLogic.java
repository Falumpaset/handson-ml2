package de.immomio.broker.queue.listener.additionalLogic.property;

import de.immomio.broker.queue.listener.additionalLogic.AdditionalLogic;
import de.immomio.data.base.type.customer.LandlordCustomerPreference;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractAdditionalLogic implements AdditionalLogic<Property> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractAdditionalLogic.class);

    public static boolean doLogic(LandlordCustomer customer, LandlordCustomerPreference preference, Property flat) {
        if (customer == null || preference == null) {
            return false;
        }

        Map<String, String> map = customer.getPreferences();

        if (map == null || !map.containsKey(preference.toString())) {
            return true;
        }

        return doLogic(map.get(preference.toString()), flat);
    }

    @SuppressWarnings("unchecked")
    public static boolean doLogic(String clazz, Property property) {
        if (clazz == null || clazz.isEmpty()) {
            return false;
        }

        try {
            return doLogic((Class<AdditionalLogic<Property>>) Class.forName(clazz), property);
        } catch (Exception e) {
            LOG.error("Error executing additional flat-logic ...", e);
            return false;
        }
    }

    public static boolean doLogic(Class<AdditionalLogic<Property>> clazz, Property flat) {
        if (clazz == null || flat == null) {
            return false;
        }

        AdditionalLogic<Property> logic;
        try {
            logic = clazz.newInstance();
        } catch (Exception e) {
            LOG.error("Error executing additional flat-logic ...", e);
            return false;
        }

        return logic.doLogic(flat);
    }
}
