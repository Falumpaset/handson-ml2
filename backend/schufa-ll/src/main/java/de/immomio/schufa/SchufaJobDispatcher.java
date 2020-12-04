package de.immomio.schufa;

import de.immomio.common.encryption.CryptoUtils;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.constants.customer.Title;
import de.immomio.data.base.bean.schufa.cbi.CbiRequest;
import de.immomio.data.base.bean.schufa.cbi.SchufaAddress;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.SchufaValidationException;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheck;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.information.SchufaBankAccount;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheck;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingResponse;
import de.immomio.data.base.bean.schufa.cbi.creditRating.enums.SchufaGenderType;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.base.bean.schufa.cbi.enums.SchufaReportType;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheck;
import de.immomio.data.base.bean.schufa.cbi.identityCheck.IdentityCheckResponse;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.base.type.schufa.SchufaUserInfo;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaAccount;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.profile.details.BankAccount;
import de.immomio.model.repository.landlord.schufa.LandlordSchufaAccountRepository;
import de.immomio.model.repository.landlord.schufa.LandlordSchufaJobRepository;
import de.immomio.reporting.LandlordSchufaIndexingSenderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Date;

import static de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType.SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT;

/**
 * @author Johannes Hiemer.
 */

@Slf4j
@Component
public class SchufaJobDispatcher {

    private static final String UNSUPPORTED_ACTION_TYPE = "UNSUPPORTED_ACTION_TYPE_L";
    private static final String SCHUFA_CREDENTIALS_NULL = "SCHUFA_CREDENTIALS_NULL_L";
    private static final String SCHUFA_NO_ACTION_TYPE = "NO_ACTION_TYPE_SET_L";
    private static final String TENANT_REQUEST_TYPE = "AM";
    private static final String BANKACCOUNT_MAY_NOT_BE_NULL = "BANKACCOUNT_MAY_NOT_BE_NULL_L";
    private static final String SCHUFA_CREDENTIALS_DECRYPT_ERROR = "SCHUFA_CREDENTIALS_DECRYPT_ERROR_L";

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    private SchufaConnector schufaConnector;

    private LandlordSchufaAccountRepository schufaAccountRepository;

    private LandlordSchufaJobRepository landlordSchufaJobRepository;

    private final EntityManager entityManager;

    private final LandlordSchufaIndexingSenderService schufaIndexingService;

    @Autowired
    public SchufaJobDispatcher(
            SchufaConnector schufaConnector,
            LandlordSchufaAccountRepository schufaAccountRepository,
            LandlordSchufaJobRepository landlordSchufaJobRepository,
            EntityManager entityManager,
            LandlordSchufaIndexingSenderService schufaIndexingService
    ) {
        this.schufaConnector = schufaConnector;
        this.schufaAccountRepository = schufaAccountRepository;
        this.landlordSchufaJobRepository = landlordSchufaJobRepository;
        this.entityManager = entityManager;
        this.schufaIndexingService = schufaIndexingService;
    }

    public void dispatch(
            LandlordUser user,
            SchufaUserInfo schufaUserInfo,
            Long userId
    ) throws SchufaConnectorException, SchufaValidationException {
        LandlordSchufaAccount credential = schufaAccountRepository.findOne();
        if (credential == null) {
            throw new SchufaConnectorException("Could not find valid SCHUFA credentials");
        }

        this.dispatch(credential, user, schufaUserInfo, userId);
    }

    private void dispatch(
            LandlordSchufaAccount credential,
            LandlordUser user,
            SchufaUserInfo schufaUserInfo,
            Long userId
    ) throws SchufaConnectorException, SchufaValidationException {
        String participantPassword;
        String participantNumber;
        PropertySearcherUserProfile userProfile = null;
        if (userId != null) {
            userProfile = entityManager.find(PropertySearcherUserProfile.class, userId);
        }
        try {
            participantNumber = credential.getUsername();
            participantPassword = CryptoUtils.decryptString(encryptionKey, credential.getPassword());
        } catch (CryptoException e) {
            log.error("error at decrypting schufa credentials", e);
            throw new SchufaValidationException(SCHUFA_CREDENTIALS_DECRYPT_ERROR);
        }

        if (userProfile != null && userProfile.getData() != null) {
            schufaUserInfo.setPortrait(userProfile.getData().getPortrait());
        }

        if (StringUtils.isBlank(participantNumber) || StringUtils.isBlank(participantPassword)) {
            throw new SchufaValidationException(SCHUFA_CREDENTIALS_NULL);
        }


        CbiActionType cbiActionType = SchufaReportType.convertActionType(schufaUserInfo.getType());
        if (cbiActionType == null) {
            throw new SchufaValidationException(SCHUFA_NO_ACTION_TYPE);
        }

        LandlordSchufaJob landlordSchufaJob = new LandlordSchufaJob(
                JobState.ACCEPTED,
                cbiActionType,
                user.getCustomer(),
                userProfile,
                schufaUserInfo);

        landlordSchufaJob.setAgentInfo(new AgentInfo(user));
        try {
            CbiRequest cbiRequest = new CbiRequest(cbiActionType, String.valueOf(landlordSchufaJob.getJobId()));

            switch (cbiActionType) {
                case SCHUFA2_ANFRAGE_BONITAETSAUSKUNFT:
                    processCreditRatingCheck(
                            participantNumber,
                            participantPassword,
                            schufaUserInfo,
                            landlordSchufaJob,
                            cbiRequest);
                    break;
                case SCHUFA2_ANFRAGE_IDENTITAETS_CHECK:
                    processIdentityCheck(
                            participantNumber,
                            participantPassword,
                            schufaUserInfo,
                            landlordSchufaJob,
                            cbiRequest);
                    break;
                case SCHUFA2_ANFRAGE_KONTONUMMERN_CHECK:
                    processBankAccountCheck(
                            cbiActionType,
                            participantNumber,
                            participantPassword,
                            schufaUserInfo,
                            landlordSchufaJob);
                    break;
                default:
                    throw new SchufaConnectorException(UNSUPPORTED_ACTION_TYPE);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            landlordSchufaJob.setState(JobState.ERROR);
            throw new SchufaValidationException(e.getMessage());
        } finally {
            landlordSchufaJobRepository.save(landlordSchufaJob);
            schufaIndexingService.schufaRequested(schufaUserInfo, userProfile, user);
        }

    }

    private void processBankAccountCheck(
            CbiActionType cbiActionType,
            String participantNumber,
            String participantPassword,
            SchufaUserInfo userInfo,
            LandlordSchufaJob landlordSchufaJob
    ) throws SchufaConnectorException, SchufaValidationException {
        BankAccount userInfoBankAccount = userInfo.getBankAccount();
        if (userInfoBankAccount == null) {
            throw new SchufaValidationException(BANKACCOUNT_MAY_NOT_BE_NULL);
        }

        try {
            SchufaBankAccount bankAccount = new SchufaBankAccount(
                    userInfoBankAccount.getBankNumber(),
                    userInfoBankAccount.getAccountNumber(),
                    userInfoBankAccount.getIban());

            AccountNumberCheck accountNumberCheck = new AccountNumberCheck(participantNumber, participantPassword,
                    new CbiRequest(cbiActionType, String.valueOf(landlordSchufaJob.getJobId())),
                    new SchufaAddress(userInfo.getAddress()),
                    userInfo.getFirstname(),
                    userInfo.getName(),
                    SchufaGenderType.convertGenderType(userInfo.getGender()),
                    userInfo.getDateOfBirth(),
                    bankAccount,
                    getTitle(userInfo.getTitle()),
                    userInfo.getPlaceOfBirth());

            landlordSchufaJob.setAccountNumberCheck(accountNumberCheck);

            AccountNumberCheckResponse accountNumberCheckResponse = schufaConnector.runAccountNumberCheckJob(
                    landlordSchufaJob.getJobId(),
                    accountNumberCheck);

            landlordSchufaJob.setAccountNumberCheckResponse(accountNumberCheckResponse);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new SchufaValidationException(e.getMessage());
        }
    }

    private void processIdentityCheck(
            String participantNumber,
            String participantPassword,
            SchufaUserInfo userInfo,
            LandlordSchufaJob landlordSchufaJob,
            CbiRequest cbiRequest
    ) throws SchufaConnectorException, SchufaValidationException {
        try {
            IdentityCheck identityCheck = new IdentityCheck(
                    participantNumber,
                    participantPassword,
                    cbiRequest,
                    new SchufaAddress(userInfo.getAddress()),
                    userInfo.getFirstname(),
                    userInfo.getName(),
                    SchufaGenderType.convertGenderType(userInfo.getGender()),
                    userInfo.getDateOfBirth(),
                    getTitle(userInfo.getTitle()),
                    userInfo.getPlaceOfBirth());

            landlordSchufaJob.setIdentityCheck(identityCheck);

            IdentityCheckResponse identityCheckResponse = schufaConnector
                    .runIdentiyCheckJob(landlordSchufaJob.getJobId(), identityCheck);

            landlordSchufaJob.setIdentityCheckResponse(identityCheckResponse);
        } catch (IllegalArgumentException e) {

            log.error(e.getMessage(), e);
            throw new SchufaValidationException(e.getMessage());
        }
    }

    private void processCreditRatingCheck(
            String participantNumber,
            String participantPassword,
            SchufaUserInfo userInfo,
            LandlordSchufaJob landlordSchufaJob,
            CbiRequest cbiRequest
    ) throws SchufaConnectorException, SchufaValidationException {

        try {
            CreditRatingCheck creditRatingCheck = new CreditRatingCheck(
                    participantNumber,
                    participantPassword,
                    cbiRequest,
                    new SchufaAddress(userInfo.getAddress()),
                    userInfo.getFirstname(),
                    userInfo.getName(),
                    SchufaGenderType.convertGenderType(userInfo.getGender()),
                    userInfo.getDateOfBirth(),
                    TENANT_REQUEST_TYPE,
                    getTitle(userInfo.getTitle()),
                    userInfo.getPlaceOfBirth(),
                    userInfo.getClauseDate(),
                    userInfo.getBankAccount());
            if (userInfo.getPreAddress() != null) {
                creditRatingCheck.setPreAddress(new SchufaAddress(userInfo.getPreAddress()));
            }

            creditRatingCheck.setClauseDate(userInfo.getClauseDate());

            landlordSchufaJob.setCreditRatingCheck(creditRatingCheck);

            CreditRatingCheckResponse creditRatingInquiryResponse = schufaConnector
                    .runCreditRatingInquiryJob(
                            landlordSchufaJob.getJobId(),
                            creditRatingCheck);

            landlordSchufaJob.setCreditRatingCheckResponse(creditRatingInquiryResponse);
            creditRatingInquiryResponse.getCbiRequest().setCurrentDate(new Date());
            creditRatingInquiryResponse.getCbiRequest().setAction(SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT);
            landlordSchufaJob.setType(SCHUFA2_AUSKUNFT_BONITAETSAUSKUNFT);

            CreditRatingResponse creditRatingResponse = schufaConnector.runCreditRatingInformationJob(
                    landlordSchufaJob.getJobId(),
                    creditRatingInquiryResponse);

            landlordSchufaJob.setCreditRatingResponse(creditRatingResponse);
            landlordSchufaJob.setState(creditRatingResponse.getCbiState().getCode());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new SchufaValidationException(e.getMessage());
        }

    }

    private String getTitle(Title title) {
        return title == Title.NONE || title == null ? "" : title.key();
    }
}
