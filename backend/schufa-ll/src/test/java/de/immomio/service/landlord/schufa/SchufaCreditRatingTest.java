package de.immomio.service.landlord.schufa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheck;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingResponse;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.schufa.SchufaConnector;
import de.immomio.service.landlord.schufa.bonitaet.testcases.BonitaetsAuskunftTestcase;
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
import java.util.concurrent.atomic.AtomicReference;

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
public class SchufaCreditRatingTest {

    private static final String FILE_PATH = "src/test/resources/integration/bonitaetsAuskunftTestcases.yml";
    private BonitaetsAuskunftTestcase bonitaetsAuskunftTestcase;

    @Autowired
    private SchufaConnector schufaConnector;

    private String participantNumber = "600/00953";

    private String participantPassword = "MIGMIO05";

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            bonitaetsAuskunftTestcase = mapper
                    .readValue(new File(FILE_PATH), BonitaetsAuskunftTestcase.class);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    /**
     * CBI:
     * AKTION: SCHUFA2_ANFRAGE_BONITAETSAUSKUNFT
     * MAX_ALTER: 0s
     * VERSIONSNUMMER: 1.1
     * AKTUELLES_DATUM: 2012-03-02
     * VORGANGSNUMMER: 1-1
     * AKTUELLE_ADRESSE:
     * ORT: STOLBERG
     * PLZ: 52222
     * STRASSE: FINKENWEG 11
     * KOMMENTAR: A-Auskunft mit Score, Q-Bit und vielen Merkmalen
     * GEBURTSDATUM: 1950-05-05
     * GESCHLECHT: m
     * NACHNAME: VORRAGEND
     * VORNAME: HANS
     * MERKMALCODE: AG
     *
     * @throws SchufaConnectorException
     */
    @Test
    public void testCreditRating() {
        AtomicReference<Long> jobid = new AtomicReference<>();
        jobid.set(1000L);

        bonitaetsAuskunftTestcase.getTestcases().forEach(bonitaetsAuskunftTestcaseObject -> {
            log.info("Running Request for {} {}",
                    bonitaetsAuskunftTestcaseObject.getName(),
                    bonitaetsAuskunftTestcaseObject.getSurname());

            CreditRatingCheck schufaInquiry = new CreditRatingCheck(
                    participantNumber,
                    participantPassword,
                    bonitaetsAuskunftTestcaseObject.getCbiRequest(),
                    bonitaetsAuskunftTestcaseObject.getAddress(),
                    bonitaetsAuskunftTestcaseObject.getName(),
                    bonitaetsAuskunftTestcaseObject.getSurname(),
                    bonitaetsAuskunftTestcaseObject.getGender(),
                    bonitaetsAuskunftTestcaseObject.getDateOfBirth(),
                    bonitaetsAuskunftTestcaseObject.getAttributeCode(),
                    bonitaetsAuskunftTestcaseObject.getTitle(),
                    bonitaetsAuskunftTestcaseObject.getPlaceOfBirth(), null, null);
            if (bonitaetsAuskunftTestcaseObject.getPreAddress() != null) {
                schufaInquiry.setPreAddress(bonitaetsAuskunftTestcaseObject.getPreAddress());
            }
            try {
                CreditRatingCheckResponse schufaInquiryResponse =
                        schufaConnector.runCreditRatingInquiryJob(jobid.get(), schufaInquiry);
                schufaInquiryResponse.getCbiRequest().setAction(CbiActionType.SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT);
                schufaInquiryResponse.getCbiRequest().setCurrentDate(
                        bonitaetsAuskunftTestcaseObject.getCbiRequest().getCurrentDate());
                CreditRatingResponse schufaInformationResponse = schufaConnector
                        .runCreditRatingInformationJob(jobid.get(), schufaInquiryResponse);
                log.info(new ObjectMapper().writeValueAsString(schufaInformationResponse));
            } catch (Exception e) {
                Assert.fail(e.getMessage());
                log.error(e.getMessage(), e);
            }
        });
    }
}
