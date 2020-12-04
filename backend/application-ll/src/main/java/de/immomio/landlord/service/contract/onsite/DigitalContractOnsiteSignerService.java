package de.immomio.landlord.service.contract.onsite;

import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowBean;
import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowState;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.contract.DigitalContractAesCodeBean;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.landlord.service.contract.DigitalContractStatusService;
import de.immomio.landlord.service.contract.history.DigitalContractSignerHistoryService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.DOCUSIGN_DELIVERED;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState.INTERNAL_ONSITE_DATA_VERIFIED;

/**
 * @author Niklas Lindemann
 */

@Service
public class DigitalContractOnsiteSignerService extends AbstractDigitalContractSignerService {

    public static final String SIGNER_NOT_FOUND_L = "SIGNER_NOT_FOUND_L";
    public static final String NOT_ALLOWED_TO_ACCESS_SIGNER_L = "NOT_ALLOWED_TO_ACCESS_SIGNER_L";
    public static final String SIGNATURE_TYPE_MUST_BE_AES_OFFICE_L = "SIGNATURE_TYPE_MUST_BE_AES_OFFICE_L";
    public static final String ONSITE_DATA_VERIFIED_MUST_BE_CALLED_FIRST_L = "ONSITE_DATA_VERIFIED_MUST_BE_CALLED_FIRST_L";
    public static final String DATA_ALREADY_CONFIRMED_L = "DATA_ALREADY_CONFIRMED_L";
    public static final String SIGNER_MUST_BE_TENANT_L = "SIGNER_MUST_BE_TENANT_L";
    public static final String NOT_ALLOWED_TO_START_SIGNING_L = "NOT_ALLOWED_TO_START_SIGNING_L";
    public static final String NOT_ALLOWED_TO_GET_AES_CODE_L = "NOT_ALLOWED_TO_GET_AES_CODE_L";
    public static final String UPDATE_OF_ADDITIONAL_DOCUMENTS_NOT_POSSIBLE_L = "UPDATE_OF_ADDITIONAL_DOCUMENTS_NOT_POSSIBLE_L";
    public static final String SIGNER_MUST_BE_ONSITE_HOST_L = "SIGNER_MUST_BE_ONSITE_HOST_L";
    public static final String ONLY_HOST_CAN_ACCESS_PROCESS_SIGNING_L = "ONLY_HOST_CAN_ACCESS_PROCESS_SIGNING_L";

    private final DigitalContractRepository digitalContractRepository;

    private final DigitalContractSignerRepository digitalContractSignerRepository;

    private final DigitalContractSignerHistoryService digitalContractSignerHistoryService;

    private final DigitalContractStatusService contractStatusService;

    private final UserSecurityService userSecurityService;

    @Autowired
    public DigitalContractOnsiteSignerService(
            DigitalContractRepository digitalContractRepository,
            DigitalContractSignerRepository digitalContractSignerRepository,
            DigitalContractSignerHistoryService digitalContractSignerHistoryService,
            DigitalContractStatusService contractStatusService,
            DocuSignSignerService docuSignSignerService,
            UserSecurityService userSecurityService) {
        super(docuSignSignerService);
        this.digitalContractRepository = digitalContractRepository;
        this.digitalContractSignerRepository = digitalContractSignerRepository;
        this.digitalContractSignerHistoryService = digitalContractSignerHistoryService;
        this.contractStatusService = contractStatusService;
        this.userSecurityService = userSecurityService;
    }

    public void confirmSignerData(UUID internalSignerId) {
        DigitalContractSigner signer = getSigner(internalSignerId);
        if (signer.getType() == DigitalContractSignerType.LANDLORD) {
            throw new ApiValidationException(SIGNER_MUST_BE_TENANT_L);
        }
        if (signer.getCurrentState().getSignerState() != null && signer.getCurrentState().getSignerState().getLevel() >= INTERNAL_ONSITE_DATA_VERIFIED.getLevel()) {
            throw new ApiValidationException(DATA_ALREADY_CONFIRMED_L);
        }

        signer.setOnsiteDataVerified(new Date());
        signer = digitalContractSignerRepository.save(signer);

        List<DigitalContractSigner> digitalContractSigners = signer.getDigitalContract().getSigners();
        Optional<DigitalContractSigner> onsiteHostSignerOpt = digitalContractSigners.stream().filter(DigitalContractSigner::isOnsiteHost).findFirst();
        if (onsiteHostSignerOpt.isPresent()) {
            try {
                updateIdentityConfirmedDocument(onsiteHostSignerOpt.get(), signer);
            } catch (Exception ex) {
                throw new ApiValidationException(UPDATE_OF_ADDITIONAL_DOCUMENTS_NOT_POSSIBLE_L);
            }
        }

        digitalContractSignerHistoryService.updateSignerHistory(INTERNAL_ONSITE_DATA_VERIFIED, signer);
    }

    public DigitalContractTenantWorkflowBean startOnsiteSigning(UUID internalSignerId, String redirectUrl) {
        DigitalContractSigner signer = getSigner(internalSignerId);
        DigitalContractSignerHistoryState signerState = signer.getCurrentState().getSignerState();
        if (signer.getType() == DigitalContractSignerType.TENANT
                && signerState.getLevel() < DigitalContractSignerHistoryState.INTERNAL_ONSITE_DATA_VERIFIED.getLevel()) {
            throw new ApiValidationException(ONSITE_DATA_VERIFIED_MUST_BE_CALLED_FIRST_L);
        }
        if (signer.getType() == DigitalContractSignerType.TENANT
                && (signerState.getLevel() > DigitalContractSignerHistoryState.INTERNAL_ONSITE_DATA_VERIFIED.getLevel() && signerState != DOCUSIGN_DELIVERED)) {
            throw new ApiValidationException(NOT_ALLOWED_TO_START_SIGNING_L);
        }
        if (signer.getType() == DigitalContractSignerType.LANDLORD && !signer.isOnsiteHost()) {
            throw new ApiValidationException(SIGNER_MUST_BE_ONSITE_HOST_L);
        }
        DigitalContractEmbeddedSendingBean embeddedSigningUrl = getDocuSignSignerService().getEmbeddedSigningUrl(signer, redirectUrl);
        return DigitalContractTenantWorkflowBean
                .builder()
                .embeddedUrl(embeddedSigningUrl.getEmbeddedUrl())
                .workflowState(DigitalContractTenantWorkflowState.EMBEDDED_SIGNING)
                .build();
    }

    public DigitalContractAesCodeBean getAesCode(UUID internalSignerId) {
        DigitalContractSigner signer = getSigner(internalSignerId);
        if (signer.getType() != DigitalContractSignerType.TENANT) {
            throw new ApiValidationException(SIGNER_MUST_BE_TENANT_L);
        }
        if (!List.of(DOCUSIGN_DELIVERED, INTERNAL_ONSITE_DATA_VERIFIED).contains(signer.getCurrentState().getSignerState())) {
            throw new ApiValidationException(NOT_ALLOWED_TO_GET_AES_CODE_L);
        }

        return DigitalContractAesCodeBean.builder().aesCode(signer.getAesVerificationData().getAesCode()).build();
    }

    public void triggerUpdateContractStatus(UUID internalContractId) {
        DigitalContract digitalContract = digitalContractRepository.findByInternalContractId(internalContractId);
        contractStatusService.triggerUpdateContractStatus(digitalContract.getDocuSignEnvelopeId());
    }

    private DigitalContractSigner getSigner(UUID internalSignerId) {
        LandlordUser principalUser = userSecurityService.getPrincipalUser();

        DigitalContractSigner signer = getSignerFromInternalId(internalSignerId, principalUser);
        validateSigner(signer);
        return signer;
    }

    private DigitalContractSigner getSignerFromInternalId(UUID internalSignerId, LandlordUser principalUser) {

        DigitalContractSigner signer = digitalContractSignerRepository.findByInternalSignerId(internalSignerId).orElseThrow(() -> new ApiValidationException(SIGNER_NOT_FOUND_L));
        if (!signer.getDigitalContract().getCustomer().equals(principalUser.getCustomer())) {
            throw new ApiValidationException(NOT_ALLOWED_TO_ACCESS_SIGNER_L);
        }
        return signer;
    }

    private void validateSigner(DigitalContractSigner signer) {
        if (signer.getDigitalContract().getSignatureType() != DigitalContractSignatureType.AES_OFFICE) {
            throw new ApiValidationException(SIGNATURE_TYPE_MUST_BE_AES_OFFICE_L);
        }
        if (!signer.getDigitalContract().getOnsiteHostAgent().equals(userSecurityService.getPrincipalUser())) {
            throw new ApiValidationException(ONLY_HOST_CAN_ACCESS_PROCESS_SIGNING_L);
        }
    }

}
