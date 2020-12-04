package de.immomio.importer.converter.openimmo;

import de.immomio.constants.exceptions.ImmomioTechnicalException;
import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
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
class OpenImmoToImmomioConverterFlatTest {

    @Spy
    private OpenImmoToImmomioConverterEnergyCertificate openImmoToImmomioConverterEnergyCertificate;

    @InjectMocks
    private OpenImmoToImmomioConverterFlat openImmoToImmomioConverterFlat;

    @ParameterizedTest(name = "run flat create Property from flat Immobilie for {arguments}")
    @ValueSource(strings = {"flat",
            "gwhNoJahrgang",
            "FlatEverythingNullExceptObjektTyp"})
    public void createFlatPropertyFromOpenImmoFlat(String fileName) throws JAXBException, IOException, SAXException,
            OpenImmoToImmomioConverterException, ImmomioTechnicalException {
        Immobilie immobilie = TestUtils.loadImmobilie(this, "/converter/openimmo/flat/given/" + fileName + ".xml");
        Property expectedProperty = TestUtils.loadProperty(this,
                "/converter/openimmo/flat/expected/" + fileName + ".json");

        Property property = new Property();
        property.setData(new PropertyData());

        openImmoToImmomioConverterFlat.addImmobilie(property, immobilie);

        TestUtils.assertDiffs(expectedProperty, property);
    }
}