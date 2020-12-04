package de.immomio.service.contract.aes;

import de.immomio.beans.TokenBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractAesStateBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowState;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractConfirmSchufaTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractSigningTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractStartAesTokenBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractVerifyAesCodeTokenBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.SignerCurrentStateBean;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistory;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerHistoryRepository;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.service.contract.DigitalContractHistoryService;
import de.immomio.service.contract.DigitalContractSignerHistoryService;
import de.immomio.service.contract.DigitalContractSignerService;
import de.immomio.service.contract.DigitalContractValidationService;
import de.immomio.service.contract.notification.DigitalContractNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.DOCUSIGN_DELIVERED;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_FLAT_VISITED_UPDATED;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_ITP_CODE_VERIFICATION_FAILED;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_ITP_CODE_VERIFIED;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_ITP_MAIL_SENT;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_SCHUFA_FAILED;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_SCHUFA_SUCCESS;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_SCHUFA_SUCCESS_AFTER_CONFIRMATION;

/**
 * @author Niklas Lindemann
 */
@Service
public class DigitalContractAesService {
    public static final String USER_NOT_ALLOWED_TO_SUBMIT_AES_CODE = "USER_NOT_ALLOWED_TO_SUBMIT_AES_CODE";
    public static final String NOT_ALLOWED_TO_GET_SIGNING_URL_L = "NOT_ALLOWED_TO_GET_SIGNING_URL_L";
    private final DigitalContractSignerService signerService;
    private final DigitalContractSchufaService schufaService;
    private final DigitalContractItpService itpService;
    private final DocuSignSignerService docuSignSignerService;
    private final BaseDigitalContractSignerHistoryRepository signerHistoryRepository;
    private final BaseDigitalContractSignerRepository signerRepository;

    private final DigitalContractNotificationService notificationService;
    private final DigitalContractSignerHistoryService digitalContractSignerHistoryService;
    private final DigitalContractHistoryService digitalContractHistoryService;
    private final DigitalContractValidationService validationService;

    @Value("${contract.aes.code.maxtries}")
    private Integer maxCodeSubmitTries;

    @Autowired
    public DigitalContractAesService(
            DigitalContractSignerService signerService,
            DigitalContractSchufaService schufaService,
            DigitalContractItpService itpService,
            DocuSignSignerService docuSignSignerService,
            BaseDigitalContractSignerHistoryRepository signerHistoryRepository,
            BaseDigitalContractSignerRepository signerRepository,
            DigitalContractNotificationService notificationService,
            DigitalContractSignerHistoryService digitalContractSignerHistoryService,
            DigitalContractHistoryService digitalContractHistoryService,
            DigitalContractValidationService validationService) {
        this.signerService = signerService;
        this.schufaService = schufaService;
        this.itpService = itpService;
        this.docuSignSignerService = docuSignSignerService;
        this.signerHistoryRepository = signerHistoryRepository;
        this.signerRepository = signerRepository;
        this.notificationService = notificationService;
        this.digitalContractSignerHistoryService = digitalContractSignerHistoryService;
        this.digitalContractHistoryService = digitalContractHistoryService;
        this.validationService = validationService;
    }

    public DigitalContractAesStateBean processAesCheck(DigitalContractStartAesTokenBean tokenBean) throws SchufaConnectorException {
        DigitalContractSigner signer = signerService.getSignerFromToken(tokenBean.getToken());

        validationService.validateContractFinished(signer.getDigitalContract());
        validationService.validateCurrentSignerState(signer, INTERNAL_SCHUFA_SUCCESS);
        validationService.validateSignerTypeTenant(signer);

        signerService.setDateOfBirth(signer, tokenBean.getDateOfBirth());
        String iban = tokenBean.getIban().replaceAll(" ", "");
        DigitalContractSchufaState digitalContractSchufaState = schufaService.processSchufaCheck(signer, iban);

        DigitalContractAesStateBean.DigitalContractAesStateBeanBuilder stateBeanBuilder =
                DigitalContractAesStateBean.builder().schufaState(digitalContractSchufaState);

        if (digitalContractSchufaState == DigitalContractSchufaState.SUCCESS) {
            createHistoryEntry(signer, INTERNAL_SCHUFA_SUCCESS);
            DigitalContractItpState itpState = itpService.processItpCheck(
                    signer,
                    iban,
                    schufaService.isTestIban(iban)
            );
            stateBeanBuilder = stateBeanBuilder.itpState(itpState);
        }
        if (digitalContractSchufaState == DigitalContractSchufaState.MAX_SCHUFA_REQUESTS_EXCEEDED) {
            digitalContractSignerHistoryService.updateSignerHistory(INTERNAL_SCHUFA_FAILED, signer);
            digitalContractHistoryService.historyInternalCanceled(signer.getDigitalContract());
            notificationService.notifyLandlordAesFailed(signer);
        }

        return stateBeanBuilder.build();
    }

    public DigitalContractAesStateBean confirmAesData(DigitalContractConfirmSchufaTokenBean tokenBean) {
        DigitalContractSigner signer = signerService.getSignerFromToken(tokenBean.getToken());

        validationService.validateSignerTypeTenant(signer);
        validationService.validateContractFinished(signer.getDigitalContract());

        DigitalContractSchufaState digitalContractSchufaState =
                schufaService.confirmSchufaData(signer, tokenBean.isConfirmed());
        DigitalContractAesStateBean.DigitalContractAesStateBeanBuilder stateBeanBuilder =
                DigitalContractAesStateBean.builder().schufaState(digitalContractSchufaState);

        if (digitalContractSchufaState == DigitalContractSchufaState.SUCCESS_AFTER_CONFIRMATION) {
            createHistoryEntry(signer, INTERNAL_SCHUFA_SUCCESS_AFTER_CONFIRMATION);
            DigitalContractItpState itpState = itpService.processItpCheck(
                    signer,
                    signer.getAesVerificationData().getTemporaryIban(),
                    schufaService.isTestIban(signer.getAesVerificationData().getTemporaryIban())
            );
            stateBeanBuilder = stateBeanBuilder.itpState(itpState);
        }

        return stateBeanBuilder.build();
    }

    public DigitalContractTenantWorkflowBean verifyAesCode(DigitalContractVerifyAesCodeTokenBean tokenBean) {
        DigitalContractSigner signer = signerService.getSignerFromToken(tokenBean.getToken());

        validationService.validateSignerTypeTenant(signer);
        validationService.validateCurrentSignerState(signer, INTERNAL_ITP_CODE_VERIFIED);
        validationService.validateContractFinished(signer.getDigitalContract());

        if (signer.getCurrentState().getSignerState().getLevel() > INTERNAL_ITP_MAIL_SENT.getLevel()) {
            throw new ApiValidationException(USER_NOT_ALLOWED_TO_SUBMIT_AES_CODE);
        }
        signerService.increaseCodeSubmitTries(signer);
        if (tokenBean.getAesCode().equalsIgnoreCase(signer.getAesVerificationData().getAesCode())) {
            createHistoryEntry(signer, INTERNAL_ITP_CODE_VERIFIED);
            DigitalContractEmbeddedSendingBean signingUrl =
                    docuSignSignerService.getEmbeddedSigningUrl(signer, tokenBean.getRedirectUrl());
            return DigitalContractTenantWorkflowBean.builder()
                    .workflowState(DigitalContractTenantWorkflowState.AES_CODE_OK)
                    .embeddedUrl(signingUrl.getEmbeddedUrl())
                    .build();
        }
        DigitalContractTenantWorkflowState workflowState = DigitalContractTenantWorkflowState.AES_CODE_RETRY;
        Integer codeSubmitTries = signer.getAesVerificationData().getCodeSubmitTries();
        if (codeSubmitTries >= maxCodeSubmitTries) {
            createHistoryEntry(signer, INTERNAL_ITP_CODE_VERIFICATION_FAILED);
            digitalContractHistoryService.historyInternalCanceled(signer.getDigitalContract());
            notificationService.notifyLandlordAesFailed(signer);
            workflowState = DigitalContractTenantWorkflowState.AES_CODE_FAILED;
        }

        return DigitalContractTenantWorkflowBean.builder()
                .workflowState(workflowState)
                .build();
    }

    public SignerCurrentStateBean getCurrentState(TokenBean tokenBean) {
        DigitalContractSigner signer = signerService.getSignerFromToken(tokenBean.getToken());
        return signer.getCurrentState();
    }

    public DigitalContractTenantWorkflowBean getSigningUrl(DigitalContractSigningTokenBean tokenBean) {
        DigitalContractSigner signer = signerService.getSignerFromToken(tokenBean.getToken());
        DigitalContract contract = signer.getDigitalContract();
        if (contract.getSignatureType() == DigitalContractSignatureType.AES_MAIL && !List.of(DOCUSIGN_DELIVERED, INTERNAL_ITP_CODE_VERIFIED).contains(signer.getCurrentState().getSignerState())) {
            throw new ApiValidationException(NOT_ALLOWED_TO_GET_SIGNING_URL_L);
        }
        if (contract.getSignatureType() == DigitalContractSignatureType.ES_MAIL && !List.of(DOCUSIGN_DELIVERED, INTERNAL_FLAT_VISITED_UPDATED).contains(signer.getCurrentState().getSignerState())) {
            throw new ApiValidationException(NOT_ALLOWED_TO_GET_SIGNING_URL_L);
        }
        DigitalContractEmbeddedSendingBean embeddedSigningUrl =
                docuSignSignerService.getEmbeddedSigningUrl(signer, tokenBean.getRedirectUrl());

        return DigitalContractTenantWorkflowBean.builder()
                .embeddedUrl(embeddedSigningUrl.getEmbeddedUrl())
                .internalContractId(signer.getDigitalContract().getInternalContractId())
                .workflowState(DigitalContractTenantWorkflowState.AES_CODE_OK).build();
    }

    private void createHistoryEntry(DigitalContractSigner signer, DigitalContractSignerHistoryState state) {
        DigitalContractSignerHistory history = new DigitalContractSignerHistory();
        history.setSigner(signer);
        history.setState(state);
        signerHistoryRepository.save(history);

        signer.getCurrentState().setSignerState(history.getState());
        signerRepository.save(signer);
    }

}
