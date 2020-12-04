package de.immomio.landlord.service.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.beans.shared.contract.DigitalContractDataBean;
import de.immomio.beans.shared.contract.DigitalContractSignatureMappingBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.contract.DigitalContractApiUser;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.docusign.service.DocuSignService;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.landlord.service.contract.history.DigitalContractHistoryService;
import de.immomio.landlord.service.product.quota.LandlordQuotaUsageService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.user.LandlordUserService;
import de.immomio.model.repository.shared.contract.DigitalContractApiUserRepository;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.shared.contract.AbstractDigitalContractService;
import de.immomio.service.shared.contract.DigitalContractDataConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DigitalContractService extends AbstractDigitalContractService {

    public static final String CONTRACT_ALREADY_UPDATED_ONCE_L = "CONTRACT_ALREADY_UPDATED_ONCE_L";
    private static final String CONTRACT_BEAN_OR_ENVELOPE_ID_NULL_L = "CONTRACT_BEAN_OR_ENVELOPE_ID_NULL_L";
    private static final String CONTRACT_BEAN_WITH_ENVELOPE_ID_NOT_FOUND_L =
            "CONTRACT_BEAN_WITH_ENVELOPE_ID_NOT_FOUND_L";
    private static final String SIGNER_INTERNAL_ID_NOT_ALLOWED_L = "SIGNER_INTERNAL_ID_NOT_ALLOWED_TO_ACCESS_L";
    private static final String TOKEN_COULD_NOT_BE_PARSED_L = "TOKEN_COULD_NOT_BE_PARSED_L";
    private final DigitalContractHistoryService digitalContractHistoryService;
    private final DigitalContractRepository digitalContractRepository;
    private final DigitalContractSignerService digitalContractSignerService;
    private final DigitalContractApiUserRepository digitalContractApiUserRepository;
    private final DigitalContractDataConverter digitalContractDataConverter;
    private final UserSecurityService userSecurityService;
    private final DocuSignService docuSignService;
    private final LandlordQuotaUsageService quotaUsageService;
    private final DigitalContractNotificationService digitalContractNotificationService;
    private final JWTTokenService jwtTokenService;

    private final LandlordUserService landlordUserService;

    @Value("${quota.usage.validate}")
    private Boolean validateQuotaUsage;

    @Autowired
    public DigitalContractService(
            DigitalContractHistoryService digitalContractHistoryService,
            DigitalContractRepository digitalContractRepository,
            DigitalContractSignerService digitalContractSignerService,
            DigitalContractApiUserRepository digitalContractApiUserRepository,
            DigitalContractDataConverter digitalContractDataConverter,
            UserSecurityService userSecurityService,
            DocuSignService docuSignService,
            LandlordQuotaUsageService quotaUsageService,
            DigitalContractNotificationService digitalContractNotificationService,
            JWTTokenService jwtTokenService,
            LandlordUserService landlordUserService) {
        this.digitalContractHistoryService = digitalContractHistoryService;
        this.digitalContractRepository = digitalContractRepository;
        this.digitalContractSignerService = digitalContractSignerService;
        this.digitalContractApiUserRepository = digitalContractApiUserRepository;
        this.digitalContractDataConverter = digitalContractDataConverter;
        this.userSecurityService = userSecurityService;
        this.docuSignService = docuSignService;
        this.quotaUsageService = quotaUsageService;
        this.digitalContractNotificationService = digitalContractNotificationService;
        this.jwtTokenService = jwtTokenService;
        this.landlordUserService = landlordUserService;
    }

    public DigitalContractDataBean getContractBeanByInternalContractId(final UUID internalContractId) {
        DigitalContract digitalContract = digitalContractRepository.findByInternalContractId(internalContractId);

        return digitalContractDataConverter.convertContractEntityToBean(digitalContract);
    }

    public DigitalContractEmbeddedSendingBean createDigitalContract(final DigitalContractDataBean contractBean) {
        if (validateQuotaUsage) {
            quotaUsageService.validateQuota(QuotaProductType.DIGITAL_CONTRACT);
        }

        LandlordUser user = userSecurityService.getPrincipalUser();
        DigitalContract digitalContract =
                digitalContractDataConverter.convertContractBeanToEntity(contractBean, user);

        digitalContract.setOnsiteHostAgent(landlordUserService.findById(contractBean.getOnsiteHostAgentId()).orElse(null));
        digitalContractRepository.save(digitalContract);
        List<DigitalContractSigner> signers = digitalContractSignerService.mergeSignersIntoNewContract(digitalContract, contractBean);
        digitalContractHistoryService.historyInternalCreated(digitalContract);

        try {
            DigitalContractApiUser apiUser = digitalContractApiUserRepository.findByCustomer(user.getCustomer()).orElseThrow();
            DigitalContractEmbeddedSendingBean embeddedSendingBean = createEnvelope(contractBean, digitalContract, signers, apiUser.getDocuSignApiUserId());
            embeddedSendingBean.setInternalContractId(digitalContract.getInternalContractId());

            return embeddedSendingBean;
        } catch (DocuSignApiException e) {
            digitalContractHistoryService.historyDocuSignCreateFailed(digitalContract, e);

            throw e;
        }
    }

    public DigitalContractEmbeddedSendingBean updateDigitalContract(final DigitalContractDataBean contractBean) {
        if (contractBean == null || contractBean.getInternalContractId() == null) {
            throw new ApiValidationException(CONTRACT_BEAN_OR_ENVELOPE_ID_NULL_L);
        }

        DigitalContract contractFromDb =
                digitalContractRepository.findByInternalContractId(contractBean.getInternalContractId());
        if (contractFromDb == null) {
            log.error("No entry for envelopeId '{}' found.", contractBean.getInternalContractId());
            throw new ApiValidationException(CONTRACT_BEAN_WITH_ENVELOPE_ID_NOT_FOUND_L);
        }
        if (contractAlreadySigned(contractFromDb)) {
            digitalContractHistoryService.historyInternalCanceled(contractFromDb);
            return createDigitalContract(contractBean);
        }

        if (contractFromDb.isAlreadyUpdated()) {
            throw new ApiValidationException(CONTRACT_ALREADY_UPDATED_ONCE_L);
        }

        boolean wasInterrupted = contractFromDb.getCurrentState() == DigitalContractHistoryState.INTERNAL_INTERRUPTED;

        DigitalContract digitalContract =
                digitalContractDataConverter.mergeDigitalContract(contractFromDb, contractBean);
        digitalContract = digitalContractRepository.save(digitalContract);

        List<DigitalContractSigner> signers = digitalContractSignerService.mergeSignersIntoExistingContract(digitalContract, contractBean);
        digitalContractHistoryService.historyInternalUpdated(digitalContract);

        try {
            DigitalContractApiUser apiUser =
                    digitalContractApiUserRepository.findByCustomer(digitalContract.getCustomer()).orElseThrow();
            DigitalContractEmbeddedSendingBean dcUpdateResponse;
            if (contractFromDb.getCurrentState() == DigitalContractHistoryState.EXTERNAL_CREATED || contractFromDb.getDocuSignEnvelopeId() == null) {
                dcUpdateResponse = createEnvelopeForExternalCreatedContract(contractBean, digitalContract, signers, apiUser);
            } else {
                dcUpdateResponse = updateExistingEnvelope(contractBean, wasInterrupted, digitalContract, signers, apiUser);
            }
            dcUpdateResponse.setInternalContractId(digitalContract.getInternalContractId());

            return dcUpdateResponse;
        } catch (DocuSignApiException e) {
            digitalContractHistoryService.historyDocuSignUpdateFailed(digitalContract, e);

            throw e;
        }
    }

    public DigitalContractEmbeddedSendingBean getSignatureMappingUrl(
            final DigitalContractSignatureMappingBean digitalContractSignatureMappingBean
    ) {
        LandlordUser user = userSecurityService.getPrincipalUser();
        DigitalContractApiUser apiUser = digitalContractApiUserRepository.findByCustomer(user.getCustomer()).orElseThrow();
        DigitalContract contract = digitalContractRepository.findByInternalContractId(digitalContractSignatureMappingBean.getInternalContractId());
        return docuSignService.getSignatureMappingUrl(
                contract.getDocuSignEnvelopeId(),
                digitalContractSignatureMappingBean.getEmbeddedSendingRedirectUrl(),
                apiUser.getDocuSignApiUserId()
        );
    }

    public void resendSignMail(UUID externalSignerId, LandlordUser principal) {
        DigitalContractSigner signer = digitalContractSignerService.findSignerByInternalId(externalSignerId);
        validatePrincipalUserForContract(signer.getDigitalContract(), principal);

        try {
            digitalContractNotificationService.resendSignerEmail(signer, jwtTokenService.generateDigitalContractSignToken(signer.getId()));
        } catch (JsonProcessingException e) {
            log.error((e.getMessage()));
            throw new ImmomioRuntimeException(TOKEN_COULD_NOT_BE_PARSED_L);
        }
    }

    private boolean contractAlreadySigned(DigitalContract contractFromDb) {
        return contractFromDb.getCountSignersCompleted() > 0;
    }

    private DigitalContractEmbeddedSendingBean createEnvelope(
            DigitalContractDataBean contractBean,
            DigitalContract digitalContract,
            List<DigitalContractSigner> signers,
            UUID apiUserId
    ) {
        DigitalContractEmbeddedSendingBean dcCreateResponse = docuSignService.createEnvelope(
                digitalContract,
                contractBean.getEmbeddedSendingRedirectUrl(),
                apiUserId,
                signers);
        digitalContractSignerService.saveAllSigners(signers);
        digitalContractRepository.save(digitalContract);
        quotaUsageService.quotaUsed(QuotaProductType.DIGITAL_CONTRACT);
        digitalContractHistoryService.historyDocuSignCreated(digitalContract);

        return dcCreateResponse;
    }

    private DigitalContractEmbeddedSendingBean updateExistingEnvelope(
            DigitalContractDataBean contractBean,
            boolean wasInterrupted,
            DigitalContract digitalContract,
            List<DigitalContractSigner> signers,
            DigitalContractApiUser apiUser
    ) {
        DigitalContractEmbeddedSendingBean dcUpdateResponse;
        dcUpdateResponse = docuSignService.updateEnvelope(
                digitalContract,
                contractBean.getEmbeddedSendingRedirectUrl(),
                apiUser.getDocuSignApiUserId(),
                signers);

        digitalContractRepository.save(digitalContract);
        digitalContractHistoryService.historyInternalUpdated(digitalContract);
        if (wasInterrupted) {
            digitalContractHistoryService.historyDocusignUpdatedAfterInterrupted(digitalContract);
        } else {
            digitalContractHistoryService.historyDocuSignUpdated(digitalContract);
        }
        return dcUpdateResponse;
    }

    private DigitalContractEmbeddedSendingBean createEnvelopeForExternalCreatedContract(
            DigitalContractDataBean contractBean,
            DigitalContract digitalContract,
            List<DigitalContractSigner> signers,
            DigitalContractApiUser apiUser
    ) {
        DigitalContractEmbeddedSendingBean dcUpdateResponse;
        dcUpdateResponse = createEnvelope(
                contractBean,
                digitalContract,
                signers,
                apiUser.getDocuSignApiUserId());
        if (validateQuotaUsage) {
            quotaUsageService.validateQuota(QuotaProductType.DIGITAL_CONTRACT);
        }
        return dcUpdateResponse;
    }

    private void validatePrincipalUserForContract(DigitalContract contract, LandlordUser user) {
        if (!contract.getCustomer().equals(user.getCustomer())) {
            throw new ApiValidationException(SIGNER_INTERNAL_ID_NOT_ALLOWED_L);
        }
    }

}
