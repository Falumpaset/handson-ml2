package utils;

import de.immomio.data.landlord.entity.property.Property;

import static org.junit.Assert.assertEquals;

public class TestComparatorHelper {

    public static void compareProperty(Property first, Property second) {
        assertEquals(first.getId(), second.getId());
        assertEquals(first.getData(), second.getData());
    }
}
