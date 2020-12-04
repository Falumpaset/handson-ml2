package de.immomio.schufa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheck;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheck;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingResponse;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheck;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheckResponse;
import de.insiders.dss.core.DSSReply;
import de.insiders.dss.prod.ConnectionStatus;
import de.insiders.dss.prod.ISREService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Service
public class SchufaConnector {

    private static final String SPECIAL_CHARS_TXT = "/specialChars.txt";
    private static final String ERROR = "__ERROR";

    private SchufaConfig schufaConfig;

    private SchufaProdConfig schufaProdConfig;

    private ObjectMapper objectMapper;

    private ISREService isreService;

    private Map<Character, Character> specialChars;

    @Autowired
    public SchufaConnector(SchufaConfig schufaConfig, SchufaProdConfig schufaProdConfig) {
        this.schufaConfig = schufaConfig;
        this.schufaProdConfig = schufaProdConfig;
    }

    @PostConstruct
    public void init() {
        try {
            specialChars = new HashMap<>();
            Files.lines(Paths.get(getClass().getResource(SPECIAL_CHARS_TXT).toURI())).forEach(this::appendMap);
        } catch (Exception e) {
            log.error("failed to initialize special char map");
        }

        createSchufaObjectMapper();
    }

    private void createSchufaObjectMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void appendMap(String line) {
        String[] split = line.split("");
        if (split.length < 2) {
            return;
        }
        specialChars.put(split[0].charAt(0), split[1].charAt(0));
    }

    private void createConnection() throws SchufaConnectorException {
        Assert.notNull(schufaConfig, "schufaConfig may not be null");
        Assert.notNull(schufaConfig.getHost(), "schufaConfig (Host) may not be null");
        Assert.notNull(schufaConfig.getPort(), "schufaConfig (Port) may not be null");

        try {
            isreService = new ISREService(schufaConfig.getHost(), schufaConfig.getPort());
        } catch (IOException ex) {
            throw new SchufaConnectorException("Could not create connection to CBI", ex);
        }

        if (!isConnected()) {
            throw new SchufaConnectorException("Could not validate connection to CBI");
        }
    }

    private void createProdConnection() throws SchufaConnectorException {
        Assert.notNull(schufaProdConfig, "schufaConfig may not be null");
        Assert.notNull(schufaProdConfig.getHost(), "schufaConfig (Host) may not be null");
        Assert.notNull(schufaProdConfig.getPort(), "schufaConfig (Port) may not be null");

        try {
            isreService = new ISREService(schufaProdConfig.getHost(), schufaProdConfig.getPort());
        } catch (IOException ex) {
            throw new SchufaConnectorException("Could not create connection to CBI", ex);
        }

        if (!isConnected()) {
            throw new SchufaConnectorException("Could not validate connection to CBI");
        }
    }

    private boolean isConnected() {
        log.debug("Checking status of DSS ...");
        ConnectionStatus[] status = isreService.checkAllConnections();
        ConnectionStatus firstStatus = status[0];
        log.info("Current status of DSS Instance on {}@{} is: {}",
                firstStatus.getHostname(),
                firstStatus.getPort(),
                (firstStatus.isStatusOk() ? "OK" : "Broken"));

        return firstStatus.isStatusOk();
    }

    private void closeConnection() throws SchufaConnectorException {
        log.debug("Shutting down the ISREService ...");

        try {
            if (isreService != null) {
                isreService.exit();
            }
        } catch (IOException ex) {
            throw new SchufaConnectorException("An IOException happened while " +
                    "trying to shut down the ISREService.", ex);
        }

        log.debug("Shutdown finished.");
    }

    /**
     * The Schufa Connector enables us to validate different scenarios:
     * Schufa BonitätsAnfrage
     * Schufa Bonitätsauskunft
     * <p>
     * Possible Error States
     * 00 - No error
     * 04 - Warning/Information
     * 08 - Error
     * 12 - Fatal error
     * <p>
     * See handleResponse for details about error handling
     *
     * @return
     * @throws SchufaConnectorException
     */
    private DSSReply runJob(String jobId, String jobRequest, boolean useProd) throws SchufaConnectorException {
        log.debug("Sending a request to the DSS ...");

        Assert.notNull(jobId, "jobId may not be null");
        Assert.notNull(jobRequest, "jobRequest may not be null");

        if (useProd) {
            this.createProdConnection();
        } else {
            this.createConnection();
        }

        DSSReply dssReply;
        try {
            dssReply = isreService.executeRequest("JSON", "JSON",
                    jobId, jobRequest);

            this.handleResponse(dssReply);

            log.info("DSS replied with ReturnCode: " +
                            "{} and ReasonCode {} and Error {}",
                    dssReply.getReply(),
                    dssReply.getReasonCode(),
                    dssReply.getReply());
        } catch (IOException ex) {
            throw new SchufaConnectorException("An Exception happened " +
                    "while trying to send a request to a DSS Instance", ex);
        } finally {
            this.closeConnection();
        }

        return dssReply;
    }

    /**
     * The Schufa Connector enables us to validate different scenarios:
     * Schufa BonitätsAnfrage
     * Schufa Bonitätsauskunft
     * <p>
     * Possible Error States
     * 00 - No error
     * 04 - Warning/Information
     * 08 - Error
     * 12 - Fatal error
     * <p>
     * See handleResponse for details about error handling
     *
     * @param jobId
     * @param jobRequest
     * @return
     * @throws SchufaConnectorException
     */
    private DSSReply runJob(String jobId, String jobRequest) throws SchufaConnectorException {
       return runJob(jobId, jobRequest, false);
    }

    /**
     * This method is running a job to instantiate a Bonitaetsauskunft
     *
     * @param schufaInquiry
     * @return
     * @throws SchufaConnectorException
     */

    public CreditRatingCheckResponse runCreditRatingInquiryJob(
            long jobId,
            CreditRatingCheck schufaInquiry
    ) throws SchufaConnectorException {
        CreditRatingCheckResponse schufaInquiryResponse;
        try {
            DSSReply dssReply = this.runJob(String.valueOf(jobId),
                    replaceSpecialChars(objectMapper.writeValueAsString(schufaInquiry)));

            schufaInquiryResponse = objectMapper.readValue(dssReply.getReply(), CreditRatingCheckResponse.class);
        } catch (SchufaConnectorException | IOException ex) {
            throw new SchufaConnectorException("An Exception happened " +
                    "while trying to send a request to a DSS Instance", ex);
        }

        return schufaInquiryResponse;
    }


    public CreditRatingResponse runCreditRatingInformationJob(
            long jobId,
            CreditRatingCheckResponse schufaInquiryResponse
    ) throws SchufaConnectorException {
        CreditRatingResponse schufaInformationResponse;
        try {
            DSSReply dssReply = this.runJob(
                    String.valueOf(jobId),
                    replaceSpecialChars(objectMapper.writeValueAsString(schufaInquiryResponse)));

            schufaInformationResponse = objectMapper.readValue(dssReply.getReply(), CreditRatingResponse.class);
        } catch (IOException ex) {
            throw new SchufaConnectorException("An Exception happened " +
                    "while try to send a request to a DSS Instance", ex);
        }

        return schufaInformationResponse;
    }

    public IdentityCheckResponse runIdentiyCheckJob(
            long jobId,
            IdentityCheck schufaIdentityCheck
    ) throws SchufaConnectorException {
        IdentityCheckResponse schufaIdentityCheckResponse;
        try {
            String filteredPayload = replaceSpecialChars(objectMapper.writeValueAsString(schufaIdentityCheck));
            DSSReply dssReply = this.runJob(
                    String.valueOf(jobId),
                    filteredPayload);

            schufaIdentityCheckResponse = objectMapper.readValue(dssReply.getReply(), IdentityCheckResponse.class);
        } catch (IOException ex) {
            throw new SchufaConnectorException("An Exception happened " +
                    "while try to send a request to a DSS Instance", ex);
        }

        return schufaIdentityCheckResponse;
    }

    public AccountNumberCheckResponse runAccountNumberCheckJob(
            long jobId,
            AccountNumberCheck accountNumberCheck
    ) throws SchufaConnectorException {
        AccountNumberCheckResponse accountNumberCheckResponse;
        try {
            List<String> testibans = schufaConfig.getTestibans();
            boolean useProd = !testibans.contains(accountNumberCheck.getBankAccount().getIban());
            String filteredPayload = replaceSpecialChars(objectMapper.writeValueAsString(accountNumberCheck));
            DSSReply dssReply = this.runJob(
                    String.valueOf(jobId),
                    filteredPayload,
                    useProd);

            accountNumberCheckResponse = objectMapper.readValue(dssReply.getReply(), AccountNumberCheckResponse.class);
        } catch (IOException ex) {
            throw new SchufaConnectorException("An Exception happened " +
                    "while try to send a request to a DSS Instance", ex);
        }

        return accountNumberCheckResponse;
    }

    /**
     * Reponse Handler:
     * 0 - OK: everything is fine, so process the reply just normally
     * 4 - WARNING: but the reply is fine! So process the reply just normally
     * 8 - ERROR: We also will find an ERROR entry in the SystemLog! Trying to get the real message.
     * 12 - FATAL: We have a fatal problem. That means that the configuration of CBI is wrong or CBI is down
     *
     * @param dssReply
     */
    private void handleResponse(DSSReply dssReply) throws SchufaConnectorException {
        Assert.notNull(dssReply, "dssReply may not be null");

        switch (dssReply.getReturnCode()) {
            case 0:
                log.debug("Execution of request was fine");
                break;
            case 4:
                log.debug("Execution of request contained warning but execution was fine");
                break;
            case 8:
                if (dssReply.getReply() != null && dssReply.getReply().isEmpty()) {
                    throw new SchufaConnectorException("ERROR while calling CBI. See SystemLogs for details.");
                } else {
                    if (dssReply.getReply() != null && dssReply.getReply().contains(ERROR)) {
                        throw new SchufaConnectorException(dssReply.getReply());
                    } else {
                        throw new SchufaConnectorException("ERROR while calling CBI. See SystemLogs for details.");
                    }
                }
            default:
                throw new SchufaConnectorException("FATAL ERROR while calling CBI. Call for an administrator!");
        }
    }

    private String replaceSpecialChars(String json) {
        AtomicReference<String> output = new AtomicReference<>();
        output.set(json);
        specialChars.forEach((key, value) -> output.getAndUpdate(s -> s.replace(key, value)));

        return output.get();
    }
}
