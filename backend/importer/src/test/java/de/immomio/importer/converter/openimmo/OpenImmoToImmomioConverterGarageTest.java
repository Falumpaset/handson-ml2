package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.ImmomioTechnicalException;
import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.data.GarageData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Immobilie;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class OpenImmoToImmomioConverterGarageTest {

    @InjectMocks
    private OpenImmoToImmomioConverterGarage openImmoToImmomioConverterGarage;

    @ParameterizedTest(name = "run garage create Property from garage Immobilie for {arguments}")
    @ValueSource(strings = {"garage", "GarageEverythingNullExceptObjektTyp"})
    public void createGaragePropertyFromOpenImmoGarage(String fileName) throws JAXBException, IOException, SAXException,
            OpenImmoToImmomioConverterException, ImmomioTechnicalException {
        Immobilie immobilie = TestUtils.loadImmobilie(this, "/converter/openimmo/garage/given/" + fileName + ".xml");
        Property expectedProperty = TestUtils.loadProperty(this,
                "/converter/openimmo/garage/expected/" + fileName + ".json");

        Property property = new Property();
        property.setType(PropertyType.GARAGE);
        property.setData(new PropertyData());
        property.getData().setGarageData(new GarageData());

        openImmoToImmomioConverterGarage.addImmobilie(property, immobilie);

        TestUtils.assertDiffs(expectedProperty, property);
    }
}