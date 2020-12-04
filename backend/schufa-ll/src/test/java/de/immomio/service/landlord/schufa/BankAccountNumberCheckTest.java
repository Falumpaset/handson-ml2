package de.immomio.service.landlord.schufa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheck;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheckResponse;
import de.immomio.schufa.SchufaConnector;
import de.immomio.service.landlord.schufa.account.testcases.AccountNumberTestcase;
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
public class BankAccountNumberCheckTest {

    private static final String FILE_PATH = "src/test/resources/integration/accountNumberTestcases.yml";
    private AccountNumberTestcase accountNumberTestcase;

    @Autowired
    private SchufaConnector schufaConnector;

    private String participantNumber = "600/00953";

    private String participantPassword = "MIGMIO05";

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            accountNumberTestcase = mapper
                    .readValue(new File(FILE_PATH), AccountNumberTestcase.class);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    /**
     * VORNAME: Florian
     * NACHNAME: Schmid
     * GESCHLECHT: m
     * GEBURTSDATUM: 1963-12-07
     * AKTUELLE_ADRESSE:
     *   STRASSE: Ringgasse 24
     *   PLZ: 73883
     *   ORT: Braunschweig
     * CBI:
     *   AKTION: SCHUFA2_ANFRAGE_KONTONUMMERN_CHECK
     *   VERSIONSNUMMER: 1.1
     *   AKTUELLES_DATUM: 2013-08-01
     *   VORGANGSNUMMER: 123.a
     *   MAX_ALTER: 0s
     * BANKVERBINDUNG:
     *   BANKLEITZAHL: 55000000
     *   KONTONUMMER: 55001521
     *
     * @throws SchufaConnectorException
     */
    @Test
    public void testBankAccountNumberCheck() {
        accountNumberTestcase.getTestcases().forEach(accountNumberTestcaseObject -> {
            log.info("Running Request for {} {}", accountNumberTestcaseObject.getName(), accountNumberTestcaseObject.getSurname());

            AccountNumberCheck accountNumberCheck = new AccountNumberCheck(
                    participantNumber,
                    participantPassword,
                    accountNumberTestcaseObject.getCbiRequest(),
                    accountNumberTestcaseObject.getAddress(),
                    accountNumberTestcaseObject.getName(),
                    accountNumberTestcaseObject.getSurname(),
                    accountNumberTestcaseObject.getGender(),
                    accountNumberTestcaseObject.getDateOfBirth(),
                    accountNumberTestcaseObject.getBankAccount(),
                    accountNumberTestcaseObject.getTitle(), "");

            try {
                AccountNumberCheckResponse accountNumberCheckResponse = schufaConnector.runAccountNumberCheckJob(1000l, accountNumberCheck);
                accountNumberCheckResponse.getExecutionInformation().forEach(accountExecutionInformation -> {
                    System.out.println(accountExecutionInformation.getCheckType());
                    System.out.println(accountExecutionInformation.getResponseCode());
                    System.out.println(accountExecutionInformation.getText());
                });
            } catch (SchufaConnectorException e) {
                Assert.fail(e.getMessage());
            }
        });

    }

}
