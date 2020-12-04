package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.ImmomioTechnicalException;
import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.data.CommercialData;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.openimmo.Immobilie;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class OpenImmoToImmomioConverterCommercialTest {

    @Spy
    private OpenImmoToImmomioConverterEnergyCertificate openImmoToImmomioConverterEnergyCertificate;

    @InjectMocks
    private OpenImmoToImmomioConverterCommercial openImmoToImmomioConverterCommercial;

    @ParameterizedTest(name = "run commercial create Property from commercial Immobilie for {arguments}")
    @ValueSource(strings = {"commercial",
            "withBedarfEnergyCertificate",
            "withEverythingNullExceptObjektKategorie",
            "withMinimalBedarfEnergyCertificate",
            "withVerbrauchEnergyCertificate"})
    public void createCommercialPropertyFromOpenImmoCommercial(String fileName) throws JAXBException, IOException,
            SAXException, OpenImmoToImmomioConverterException, ImmomioTechnicalException {
        Immobilie immobilie = TestUtils.loadImmobilie(this,
                "/converter/openimmo/commercial/given/" + fileName + ".xml");
        Property expectedProperty = TestUtils.loadProperty(this,
                "/converter/openimmo/commercial/expected/" + fileName + ".json");

        Property property = new Property();
        property.setType(PropertyType.COMMERCIAL);
        property.setData(new PropertyData());
        property.getData().setCommercialData(new CommercialData());

        openImmoToImmomioConverterCommercial.addImmobilie(property, immobilie);

        TestUtils.assertDiffs(expectedProperty, property);
    }
}