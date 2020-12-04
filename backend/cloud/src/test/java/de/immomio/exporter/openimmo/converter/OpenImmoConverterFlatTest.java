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
class OpenImmoConverterFlatTest {

    @InjectMocks
    private OpenImmoConverterFlat openimmoConverterFlat;

    @ParameterizedTest(name = "run garage create Immobilie from Flat for {arguments}")
    @ValueSource(strings = {"baseWithoutEnergyCertificate_1",
            "baseWithoutEnergyCertificate_2",
            "withUsageEnergyCertificate_1"})
    public void createOpenImmoFlatFromCommercialFlat(String fileName) throws IOException, JAXBException, ImmomioTechnicalException {
        OpenImmoConverterData givenConvererDaten = loadJson(this,
                "/converter/openimmo/flat/given/" + fileName + ".json", OpenImmoConverterData.class);
        Immobilie expectedImmobilie = loadImmobilie(this, "/converter/openimmo/flat/expected/" + fileName + ".xml");

        Immobilie resultingImmobilie = openimmoConverterFlat.createImmobilie(givenConvererDaten);

        TestUtils.assertDiffs(expectedImmobilie, resultingImmobilie);
    }
}