package de.immomio.service.contract.aes;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountExecutionInformation;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheck;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.information.SchufaBankAccount;
import de.immomio.data.base.bean.schufa.cbi.creditRating.information.TechnicalError;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaHistory;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState;
import de.immomio.model.repository.shared.contract.signer.aes.DigitalContractSchufaHistoryRepository;
import de.immomio.schufa.service.SchufaIbanCheckService;
import de.immomio.service.contract.DigitalContractSignerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.CANCEL;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.DATA_NOT_CORRECT_AFTER_CONFIRMATION;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.DATA_NOT_CORRECT_NEEDS_TO_BE_RESTARTED;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.ERROR;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.MAX_SCHUFA_REQUESTS_EXCEEDED;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.SUCCESS;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.SUCCESS_AFTER_CONFIRMATION;
import static de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState.SUCCESS_DATA_NEEDS_TO_BE_CONFIRMED;
import static de.immomio.schufa.constants.SchufaConstants.BUSINESS_RELATIONSHIP_KNOWN;
import static de.immomio.schufa.constants.SchufaConstants.CHECK_DIGIT_PROCESS_NOT_AVAILABLE;
import static de.immomio.schufa.constants.SchufaConstants.CHECK_DIGIT_PROCESS_NOT_AVAILABLE_FOR_INSTITUTE;
import static de.immomio.schufa.constants.SchufaConstants.DATA_KNOWN;
import static de.immomio.schufa.constants.SchufaConstants.ERROR_IBAN_SYNTAX_FAILED;
import static de.immomio.schufa.constants.SchufaConstants.PAYMENT_ACCOUNT_KNOWN;
import static de.immomio.schufa.constants.SchufaConstants.PERSON_IS_DEAD;
import static de.immomio.schufa.constants.SchufaConstants.SCHUFA_DATA_NOT_KNOWN;
import static de.immomio.schufa.constants.SchufaConstants.SYNTAX_CHECK_NEGATIVE;
import static de.immomio.schufa.constants.SchufaConstants.SYNTAX_CORRECT;
import static de.immomio.schufa.constants.SchufaConstants.SYNTAX_NOT_CORRECT;
import static de.immomio.schufa.constants.SchufaConstants.SYNTAX_NOT_CORRECT_INSTITUTE_KNOWN;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class DigitalContractSchufaService {

    public static final String SIGNER_MUST_BE_TENANT = "SIGNER_MUST_BE_TENANT_L";
    public static final String DATA_NEED_TO_BE_CONFIRMED = "DATA_NEED_TO_BE_CONFIRMED_L";
    public static final String MAX_SCHUFA_TRIES_EXCEEDED = "MAX_SCHUFA_TRIES_EXCEEDED_L";
    public static final String SCHUFA_VERIFICATION_ALREADY_FINISHED = "SCHUFA_VERIFICATION_ALREADY_FINISHED_L";
    public static final String HISTORY_MUST_BE_IN_CORRECT_STATE = "HISTORY_MUST_BE_IN_CORRECT_STATE_L";
    public static final String ANONYM_SCHUFA_CRED = "XXXX";
    public static final String IBAN_PLACEHOLDER = "XXXXXXXXXXXXXXXXXX";

    private final SchufaIbanCheckService schufaIbanCheckService;

    private final DigitalContractSchufaHistoryRepository digitalContractSchufaHistoryRepository;

    private final DigitalContractSignerService signerService;

    @Value("${contract.aes.schufa.maxtries}")
    private Integer maxTries;

    @Autowired
    public DigitalContractSchufaService(
            SchufaIbanCheckService schufaIbanCheckService,
            DigitalContractSchufaHistoryRepository digitalContractSchufaHistoryRepository,
            DigitalContractSignerService signerService
    ) {
        this.schufaIbanCheckService = schufaIbanCheckService;
        this.digitalContractSchufaHistoryRepository = digitalContractSchufaHistoryRepository;
        this.signerService = signerService;
    }

    public DigitalContractSchufaState processSchufaCheck(DigitalContractSigner signer, String iban) throws SchufaConnectorException {

        validateForSchufaRequest(signer);

        Long schufaTries = digitalContractSchufaHistoryRepository.countSchufaTries(signer);

        if (schufaTries >= maxTries) {
            return saveSchufaState(signer, MAX_SCHUFA_REQUESTS_EXCEEDED, null);
        }

        AccountNumberCheck accountNumberCheckData = schufaIbanCheckService.getAccountNumberCheckData(signer, iban);
        AccountNumberCheckResponse schufaResponse = schufaIbanCheckService.processSchufaIbanCheck(accountNumberCheckData);

        DigitalContractSchufaState internalSchufaState = getInternalSchufaState(schufaResponse);
        saveSchufaHistory(signer, schufaResponse, accountNumberCheckData, internalSchufaState);
        saveSignerSchufaData(signer, internalSchufaState, iban);

        return internalSchufaState;
    }

    public DigitalContractSchufaState confirmSchufaData(DigitalContractSigner signer, boolean verified) {
        DigitalContractSchufaState currentSchufaState = getCurrentSchufaState(signer);
        if (currentSchufaState != SUCCESS_DATA_NEEDS_TO_BE_CONFIRMED) {
            throw new ApiValidationException(HISTORY_MUST_BE_IN_CORRECT_STATE);
        }
        if (verified) {
            return saveSchufaState(signer, SUCCESS_AFTER_CONFIRMATION, null);
        } else {
            return saveSchufaState(signer, DATA_NOT_CORRECT_AFTER_CONFIRMATION, signer.getAesVerificationData().getTemporaryIban());
        }

    }

    public boolean isTestIban(String iban) {
        return schufaIbanCheckService.isTestIban(iban);
    }

    private DigitalContractSchufaState saveSchufaState(DigitalContractSigner signer, DigitalContractSchufaState schufaState, String iban) {
        saveBaseSchufaHistory(signer, schufaState);
        saveSignerSchufaData(signer, schufaState, iban);
        return schufaState;
    }


    private void saveSignerSchufaData(DigitalContractSigner signer, DigitalContractSchufaState schufaState, String iban) {
        if (iban != null) {
            signer.getAesVerificationData().setTemporaryIban(iban);
        }
        if (iban != null && iban.length() >= 22) {
            signer.getAesVerificationData().setMaskedIban(getMaskedIban(iban));
        }
        signer.getCurrentState().setSchufaState(schufaState);
        signerService.save(signer);
    }

    private void saveSchufaHistory(DigitalContractSigner signer, AccountNumberCheckResponse schufaResponse, AccountNumberCheck accountNumberCheck, DigitalContractSchufaState schufaState) {
        DigitalContractSchufaHistory schufaHistory = createBaseSchufaHistory(signer, schufaState);
        schufaHistory.setSchufaResponse(schufaResponse);
        schufaHistory.setSchufaRequest(prepareSchufaCheckForSaving(accountNumberCheck));
        digitalContractSchufaHistoryRepository.save(schufaHistory);
    }

    private void saveBaseSchufaHistory(DigitalContractSigner signer, DigitalContractSchufaState schufaState) {
        DigitalContractSchufaHistory schufaHistory = createBaseSchufaHistory(signer, schufaState);
        digitalContractSchufaHistoryRepository.save(schufaHistory);
    }

    private DigitalContractSchufaHistory createBaseSchufaHistory(DigitalContractSigner signer, DigitalContractSchufaState schufaState) {
        DigitalContractSchufaHistory schufaHistory = new DigitalContractSchufaHistory();
        schufaHistory.setSigner(signer);
        schufaHistory.setState(schufaState);
        return schufaHistory;
    }

    private void validateForSchufaRequest(DigitalContractSigner signer) {
        DigitalContractSchufaState currentSchufaState = getCurrentSchufaState(signer);
        if (signer.getType() != DigitalContractSignerType.TENANT) {
            throw new ApiValidationException(SIGNER_MUST_BE_TENANT);
        }

        if (currentSchufaState == SUCCESS_DATA_NEEDS_TO_BE_CONFIRMED) {
            throw new ApiValidationException(DATA_NEED_TO_BE_CONFIRMED);
        }

        if (currentSchufaState == MAX_SCHUFA_REQUESTS_EXCEEDED) {
            throw new ApiValidationException(MAX_SCHUFA_TRIES_EXCEEDED);
        }

        if (currentSchufaState != null &&
                currentSchufaState != DATA_NOT_CORRECT_NEEDS_TO_BE_RESTARTED &&
                currentSchufaState != DATA_NOT_CORRECT_AFTER_CONFIRMATION &&
                currentSchufaState != ERROR &&
                currentSchufaState != CANCEL) {
            throw new ApiValidationException(SCHUFA_VERIFICATION_ALREADY_FINISHED);
        }
    }

    private DigitalContractSchufaState getInternalSchufaState(AccountNumberCheckResponse schufaResponse) {
        List<AccountExecutionInformation> executionInformation = schufaResponse.getExecutionInformation();

        List<TechnicalError> technicalError = schufaResponse.getTechnicalError();

        if (!executionInformation.isEmpty()) {
            boolean success = executionInformation.stream()
                    .allMatch(info -> DATA_KNOWN.equals(info.getResponseCode()) || SYNTAX_CORRECT.equals(info.getResponseCode()));
            if (success) {
                return SUCCESS;
            }

            boolean syntaxNotCorrect = executionInformation.stream()
                    .allMatch(info -> Arrays.asList(
                            SYNTAX_NOT_CORRECT_INSTITUTE_KNOWN,
                            SYNTAX_NOT_CORRECT,
                            CHECK_DIGIT_PROCESS_NOT_AVAILABLE,
                            CHECK_DIGIT_PROCESS_NOT_AVAILABLE_FOR_INSTITUTE).contains(info.getResponseCode()) ||
                            SYNTAX_CHECK_NEGATIVE.equals(info.getResponseCode()));
            if (syntaxNotCorrect) {
                return DATA_NOT_CORRECT_NEEDS_TO_BE_RESTARTED;
            }

            boolean dataMustBeVerified = executionInformation.stream()
                    .anyMatch(info -> Arrays.asList(PAYMENT_ACCOUNT_KNOWN, BUSINESS_RELATIONSHIP_KNOWN).contains(info.getResponseCode()));
            if (dataMustBeVerified) {
                return SUCCESS_DATA_NEEDS_TO_BE_CONFIRMED;
            }

            boolean dataNotKnown = executionInformation.stream()
                    .anyMatch(info -> Arrays.asList(PERSON_IS_DEAD, SCHUFA_DATA_NOT_KNOWN).contains(info.getResponseCode()));
            if (dataNotKnown) {
                return CANCEL;
            }
        }

        if (!technicalError.isEmpty()) {
            boolean syntaxNotCorrectError = technicalError.stream().anyMatch(oneError -> oneError.getErrorCode().equals(ERROR_IBAN_SYNTAX_FAILED));
            if (syntaxNotCorrectError) {
                return DATA_NOT_CORRECT_NEEDS_TO_BE_RESTARTED;
            }
        }

        return ERROR;
    }

    private DigitalContractSchufaState getCurrentSchufaState(DigitalContractSigner signer) {
        return signer.getCurrentState().getSchufaState();
    }

    private AccountNumberCheck prepareSchufaCheckForSaving(AccountNumberCheck accountNumberCheck) {
        AccountNumberCheck cloned = SerializationUtils.clone(accountNumberCheck);
        cloned.setParticipantNumber(ANONYM_SCHUFA_CRED);
        cloned.setParticipantPassword(ANONYM_SCHUFA_CRED);
        SchufaBankAccount bankAccount = accountNumberCheck.getBankAccount();

        String maskedIban = getMaskedIban(bankAccount.getIban());
        cloned.getBankAccount().setIban(maskedIban);

        return cloned;
    }

    private String getMaskedIban(String iban) {
        if (iban != null) {
            return new StringBuilder()
                    .append(iban, 0, 2)
                    .append(IBAN_PLACEHOLDER)
                    .append(iban, 18, 22)
                    .toString();
        }
        return null;
    }

}
