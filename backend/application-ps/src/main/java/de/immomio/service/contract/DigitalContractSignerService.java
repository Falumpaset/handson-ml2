package de.immomio.service.contract;

import de.immomio.beans.TokenBean;
import de.immomio.beans.shared.contract.DigitalContractSignBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowState;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractFlatVisitedTokenBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.DigitalContractException;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractCustomerSettings;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.security.common.bean.DigitalContractSignToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.contract.notification.DigitalContractNotificationService;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class DigitalContractSignerService extends AbstractDigitalContractSignerService {
    private final DigitalContractSignerRepository digitalContractSignerRepository;
    private final DigitalContractSignerHistoryService signerHistoryService;
    private final JWTTokenService jwtTokenService;
    private final DigitalContractNotificationService contractNotificationService;
    private final DigitalContractHistoryService digitalContractHistoryService;

    private final DigitalContractValidationService validationService;

    private static final String CONTRACT_NOT_SIGNED_YET_L = "CONTRACT_NOT_SIGNED_YET_L";
    public static final String TENANT_DID_NOT_VISIT_FLAT = "TENANT_DID_NOT_VISIT_FLAT_L";

    @Autowired
    public DigitalContractSignerService(
            DigitalContractSignerRepository digitalContractSignerRepository,
            DigitalContractSignerHistoryService signerHistoryService,
            JWTTokenService jwtTokenService,
            DocuSignSignerService docuSignSignerService,
            DigitalContractNotificationService contractNotificationService,
            DigitalContractHistoryService digitalContractHistoryService,
            DigitalContractValidationService validationService
    ) {
        super(docuSignSignerService);
        this.digitalContractSignerRepository = digitalContractSignerRepository;
        this.signerHistoryService = signerHistoryService;
        this.jwtTokenService = jwtTokenService;
        this.contractNotificationService = contractNotificationService;
        this.digitalContractHistoryService = digitalContractHistoryService;
        this.validationService = validationService;
    }

    public DigitalContractSignBean getContractSignBean(String token) {
        DigitalContractSigner signer = getSignerFromToken(token);

        return this.getContractSignBean(signer);
    }

    public DigitalContractTenantWorkflowBean flatVisited(final DigitalContractFlatVisitedTokenBean flatVisitedBean) throws DigitalContractException {
        DigitalContractSigner signer = getSignerFromToken(flatVisitedBean.getToken());

        if (signer.getType() != DigitalContractSignerType.TENANT) {
            throw new ApiValidationException("SIGNER_MUST_BE_TENANT_L");
        }
        validationService.validateSignerTypeTenant(signer);
        validationService.validateCurrentSignerState(signer, DigitalContractSignerHistoryState.INTERNAL_FLAT_VISITED_UPDATED);
        validationService.validateContractFinished(signer.getDigitalContract());

        signerHistoryService.updateSignerHistory(DigitalContractSignerHistoryState.INTERNAL_FLAT_VISITED_UPDATED, signer);
        boolean processShouldStop = processShouldStopDueToFlatNotVisited(signer, flatVisitedBean.getFlatVisited());
        if (processShouldStop) {
            contractNotificationService.notifyFlatNotVisited(signer);
            signerHistoryService.updateSignerHistory(DigitalContractSignerHistoryState.INTERNAL_STOPPED_BY_PS, signer);
            digitalContractHistoryService.historyInternalCanceled(signer.getDigitalContract());

            throw new DigitalContractException(TENANT_DID_NOT_VISIT_FLAT);
        }

        updateSignerHasFlatVisited(signer, flatVisitedBean.getFlatVisited());

        updateViewingDateCombinedDocument(signer, flatVisitedBean.getFlatVisited());

        if (signer.getDigitalContract().getSignatureType() == DigitalContractSignatureType.AES_MAIL) {
            return DigitalContractTenantWorkflowBean.builder()
                    .workflowState(DigitalContractTenantWorkflowState.AES_CHECK)
                    .build();
        }

        DigitalContractEmbeddedSendingBean embeddedSigningUrl =
                getDocuSignSignerService().getEmbeddedSigningUrl(signer, flatVisitedBean.getRedirectUrl());

        return DigitalContractTenantWorkflowBean.builder()
                .workflowState(DigitalContractTenantWorkflowState.EMBEDDED_SIGNING)
                .embeddedUrl(embeddedSigningUrl.getEmbeddedUrl())
                .internalContractId(signer.getDigitalContract().getInternalContractId())
                .build();
    }

    public void notifyLandlordDataNotCorrect(TokenBean tokenBean) {

        DigitalContractSigner signer = getSignerFromToken(tokenBean.getToken());

        validationService.validateSignerTypeTenant(signer);
        validationService.validateCurrentSignerState(signer, DigitalContractSignerHistoryState.INTERNAL_STOPPED_BY_PS);
        validationService.validateContractFinished(signer.getDigitalContract());

        signerHistoryService.updateSignerHistory(DigitalContractSignerHistoryState.INTERNAL_STOPPED_BY_PS, signer);
        contractNotificationService.notifyLandlordDataNotCorrect(signer);
        contractNotificationService.notifySignersDataNotCorrect(signer);
        digitalContractHistoryService.historyInternalInterrupted(signer.getDigitalContract());
    }

    public DigitalContractSigner getSignerFromToken(String token) {
        DigitalContractSignToken contractSignToken = jwtTokenService.validateContractSignToken(token);
        return digitalContractSignerRepository.findById(contractSignToken.getSignerId()).orElseThrow(() -> new ApiValidationException("SIGNER_NOT_FOUND_L"));
    }

    public void setDateOfBirth(DigitalContractSigner signer, Date dateOfBirth) {
        signer.getData().setDateOfBirth(dateOfBirth);
        digitalContractSignerRepository.save(signer);
    }

    public void increaseCodeSubmitTries(DigitalContractSigner signer) {
        signer.getAesVerificationData().setCodeSubmitTries(signer.getAesVerificationData().getCodeSubmitTries() + 1);
        digitalContractSignerRepository.save(signer);
    }

    public S3File getSignedContract(Long signerId, String s3Identifier) {
        DigitalContractSigner signer = digitalContractSignerRepository.findById(signerId).orElseThrow(() -> new ApiValidationException("SIGNER_NOT_FOUND_L"));
        DigitalContract digitalContract = signer.getDigitalContract();
        S3File archiveFile = digitalContract.getSignedDocumentArchiveFile();
        if (archiveFile != null && s3Identifier.equals(archiveFile.getIdentifier())) {
            return archiveFile;
        }
        S3File combinedFile = digitalContract.getSignedDocumentCombinedFile();
        if (combinedFile != null && s3Identifier.equals(combinedFile.getIdentifier())) {
            return combinedFile;
        }
        List<S3File> singleFiles = digitalContract.getSignedDocumentSingleFiles();
        if (singleFiles != null) {
            return singleFiles.stream()
                    .filter(s3File -> s3Identifier.equals(s3File.getIdentifier()))
                    .findAny()
                    .orElseThrow(() -> new ApiValidationException(CONTRACT_NOT_SIGNED_YET_L));
        }

        throw new ApiValidationException(CONTRACT_NOT_SIGNED_YET_L);
    }

    private boolean processShouldStopDueToFlatNotVisited(DigitalContractSigner signer, Date flatVisited) {
        LandlordCustomer customer = signer.getDigitalContract().getCustomer();
        DigitalContractCustomerSettings customerSettings = customer.getCustomerSettings().getContractCustomerSettings();

        return BooleanUtils.isNotTrue(customerSettings.getContinueContractWhenNotVisitedFlat()) && flatVisited == null;
    }

    private void updateSignerHasFlatVisited(DigitalContractSigner signer, Date flatVisited) {
        signer.setFlatVisited(flatVisited);

        digitalContractSignerRepository.save(signer);
    }


    public void save(DigitalContractSigner signer) {
        digitalContractSignerRepository.save(signer);
    }
}
