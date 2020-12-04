package de.immomio.exporter.openimmo.converter;

import de.immomio.constants.exceptions.ImmomioTechnicalException;
import de.immomio.openimmo.Immobilie;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static de.immomio.exporter.openimmo.converter.TestUtils.loadImmobilie;
import static de.immomio.exporter.openimmo.converter.TestUtils.loadJson;

@ExtendWith(MockitoExtension.class)
class OpenImmoConverterGarageTest {

    @InjectMocks
    private OpenImmoConverterGarage openimmoConverterGarage;

    @ParameterizedTest(name = "run garage create Immobilie from Garage for {arguments}")
    @ValueSource(strings = {"garage", "garageGarageTypeNull"})
    public void createOpenImmoGarageFromCommercialGarage(String fileName) throws IOException, JAXBException, ImmomioTechnicalException {
        OpenImmoConverterData givenConvererDaten = loadJson(this,
                "/converter/openimmo/garage/given/" + fileName + ".json", OpenImmoConverterData.class);
        Immobilie expectedImmobilie = loadImmobilie(this, "/converter/openimmo/garage/expected/" + fileName + ".xml");

        Immobilie resultingImmobilie = openimmoConverterGarage.createImmobilie(givenConvererDaten);

        TestUtils.assertDiffs(expectedImmobilie, resultingImmobilie);
    }
}