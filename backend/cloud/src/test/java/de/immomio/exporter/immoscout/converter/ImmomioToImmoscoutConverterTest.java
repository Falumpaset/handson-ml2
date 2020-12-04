package de.immomio.exporter.immoscout.converter;

import de.immobilienscout24.rest.schema.offer.realestates._1.ApartmentRent;
import de.immobilienscout24.rest.schema.offer.realestates._1.Office;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.exporter.openimmo.converter.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static de.immomio.exporter.openimmo.converter.TestUtils.loadJson;

class ImmomioToImmoscoutConverterTest {

    @ParameterizedTest(name = "run convert Office from Property for {arguments}")
    @ValueSource(strings = {"commercial", "baseOffice"})
    public void createOfficeFromProperty(String fileName) throws IOException {
        Property givenProperty = loadJson(this, "/converter/immoscout/commercial/given/" + fileName + ".json",
                Property.class);
        Office expectedOffice = loadJson(this, "/converter/immoscout/commercial/expected/" + fileName + ".json",
                Office.class);

        Office office = ImmomioToImmoscoutConverter.convertOffice(givenProperty);

        TestUtils.assertDiffs(expectedOffice, office);
    }

    @ParameterizedTest(name = "run convert ApartmentRent from Property for {arguments}")
    @ValueSource(strings = {"baseWithoutEnergyCertificate_1",
            "baseWithoutEnergyCertificate_2",
            "withUsageEnergyCertificate_1"})
    public void createApartmentRentFromProperty(String fileName) throws IOException {
        Property givenProperty = loadJson(this, "/converter/immoscout/flat/given/" + fileName + ".json",
                Property.class);
        ApartmentRent expectedApartmentRent = loadJson(this, "/converter/immoscout/flat/expected/" + fileName + ".json",
                ApartmentRent.class);

        ApartmentRent apartmentRent = ImmomioToImmoscoutConverter.convertApartmentRent(givenProperty);

        TestUtils.assertDiffs(expectedApartmentRent, apartmentRent);
    }

}