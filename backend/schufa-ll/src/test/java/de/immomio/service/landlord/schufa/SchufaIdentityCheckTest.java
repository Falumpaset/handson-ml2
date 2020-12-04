package de.immomio.service.landlord.schufa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheck;
import de.immomio.schufa.SchufaConnector;
import de.immomio.service.landlord.schufa.identity.testcases.IdentityCheckTestcase;
import de.immomio.service.landlord.schufa.identity.testcases.IdentityCheckTestcaseObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * @author Johannes Hiemer.
 */

@Ignore
@Slf4j
@ActiveProfiles(value = {"test", "development"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        CustomSchufaConfiguration.class
})
public class SchufaIdentityCheckTest {

    private static final String FILE_PATH = "src/test/resources/integration/identitaetsCheckTestcases.yml";
    private static final String participantPassword = "MIGMIO05";
    private static final String participantNumber = "600/00953";

    private IdentityCheckTestcase identityCheckTestcase;

    @Autowired
    private SchufaConnector schufaConnector;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            identityCheckTestcase = mapper
                    .readValue(new File(FILE_PATH), IdentityCheckTestcase.class);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    /**
     * VORNAME: Felixs
     * NACHNAME: Koenig
     * GEBURTSDATUM: 1993-10-12
     * GESCHLECHT: m
     * IDENTITAETSCHECK_VARIANTE: Standard
     * AKTUELLE_ADRESSE:
     *  ORT: Kauferin
     *  PLZ: 86916
     *  STRASSE: Schillerstasse 6
     * CBI:
     *  AKTION: SCHUFA2_ANFRAGE_IDENTITAETS_CHECK
     *  MAX_ALTER: 0s
     *  VERSIONSNUMMER: 1.1
     *  AKTUELLES_DATUM: 2012-03-02
     *  VORGANGSNUMMER: 25-1
     * KOMMENTAR: Person gefunden  - Gesamttreffergüte 98 Prozent
     *
     * @throws SchufaConnectorException
     */
    @Test
    public void testIdentityCheck() {
        for (IdentityCheckTestcaseObject identityCheckTestcaseObject : identityCheckTestcase.getTestcases()) {
            log.info(
                    "Running Request for {} {}",
                    identityCheckTestcaseObject.getName(),
                    identityCheckTestcaseObject.getSurname());

            IdentityCheck schufaIdentityCheck = new IdentityCheck(
                    participantNumber,
                    participantPassword,
                    identityCheckTestcaseObject.getCbiRequest(),
                    identityCheckTestcaseObject.getAddress(),
                    identityCheckTestcaseObject.getName(),
                    identityCheckTestcaseObject.getSurname(),
                    identityCheckTestcaseObject.getGender(),
                    identityCheckTestcaseObject.getDateOfBirth(),
                    identityCheckTestcaseObject.getTitle(),
                    "");
            try {
               schufaConnector.runIdentiyCheckJob(1000l, schufaIdentityCheck);
            } catch (SchufaConnectorException e) {
                Assert.fail(e.getMessage());
            }
        }
    }
}
