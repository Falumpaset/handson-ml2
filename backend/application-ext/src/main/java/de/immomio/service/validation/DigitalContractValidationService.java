package de.immomio.service.validation;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.exception.ExternalApiContractNotFinishedException;
import de.immomio.exception.ExternalApiNotFoundException;
import de.immomio.exception.ExternalApiValidationException;
import de.immomio.model.contract.ApiSignatureType;
import de.immomio.model.contract.CreateContractApiBean;
import de.immomio.model.repository.shared.contract.DigitalContractApiUserRepository;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.service.impl.UserSecurityService;
import de.immomio.utils.EmailAddressUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Service
public class DigitalContractValidationService {

    private UserSecurityService userSecurityService;
    private DigitalContractRepository digitalContractRepository;
    private DigitalContractApiUserRepository digitalContractApiUserRepository;

    @Autowired
    public DigitalContractValidationService(
            UserSecurityService userSecurityService,
            DigitalContractRepository digitalContractRepository,
            DigitalContractApiUserRepository digitalContractApiUserRepository
    ) {
        this.userSecurityService = userSecurityService;
        this.digitalContractRepository = digitalContractRepository;
        this.digitalContractApiUserRepository = digitalContractApiUserRepository;
    }

    public void validateApiUserExists() {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        digitalContractApiUserRepository.findByCustomer(customer).orElseThrow(() -> new ExternalApiNotFoundException("DOCUSIGN USER DOESNT EXIST"));
    }

    public void validateContractExists(CreateContractApiBean apiBean) {
        Optional<DigitalContract> existingContract = digitalContractRepository.findFirstByExternalId(apiBean.getId());
        if (existingContract.isPresent()) {
            throw new ExternalApiValidationException("CONTRACT WITH ID ALREADY EXISTS");
        }
    }

    public void validateApiBean(CreateContractApiBean apiBean) {
        boolean landlordMailEmpty = apiBean.getLandlordSigners().stream()
                .anyMatch(signerInfo -> StringUtils.isBlank(signerInfo.getAddress().getEmail()));

        boolean tenantMailEmpty = apiBean.getTenantSigners().stream()
                .anyMatch(signerInfo -> StringUtils.isBlank(signerInfo.getAddress().getEmail()));

        if (landlordMailEmpty || tenantMailEmpty) {
            throw new ExternalApiValidationException("SIGNER EMAILS MUST BE FILLED");
        }

        boolean landlordMailNotValid = apiBean.getLandlordSigners().stream().anyMatch(signerInfo -> EmailAddressUtils.isInvalid(signerInfo.getAddress().getEmail()));
        boolean tenantMailNotValid = apiBean.getTenantSigners().stream().anyMatch(signerInfo -> EmailAddressUtils.isInvalid(signerInfo.getAddress().getEmail()));
        if (landlordMailNotValid || tenantMailNotValid) {
            throw new ExternalApiValidationException("SIGNER EMAILS MUST BE VALID");
        }

        boolean genderNull = apiBean.getTenantSigners().stream().anyMatch(signerInfo -> signerInfo.getGender() == null);

        if (genderNull) {
            throw new ExternalApiValidationException("GENDER MUST BE SET AT TENANT SIGNERS");
        }

        if (apiBean.getSignatureType() == ApiSignatureType.ONSITE && StringUtils.isBlank(apiBean.getOnsiteHostEmailAddress())) {
            throw new ExternalApiValidationException("ONSITE HOST EMAIL ADDRESS MUST BE SET WHEN TYPE IS ONSITE");
        }
    }

    public void validateContractModuleBooked() {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        if (!customer.isDigitalContractAllowed()) {
            throw new ExternalApiValidationException("CONTRACT MODULE NOT BOOKED");
        }
    }

    public void validateContractDownload(DigitalContract contract) {
        if (contract.getCurrentState() != DigitalContractHistoryState.DOCUSIGN_COMPLETED) {
            throw new ExternalApiContractNotFinishedException("CONTRACT NOT FINISHED");
        }
    }

    public void validateUploadFiles(DigitalContract contract, MultipartFile[] files) {
        if (contract.getCurrentState() != DigitalContractHistoryState.EXTERNAL_CREATED) {
            throw new ExternalApiValidationException("CONTRACT IN UNSUITABLE STATE");
        }
        if (contract.getDocumentFiles() != null && !contract.getDocumentFiles().isEmpty()) {
            throw new ExternalApiValidationException("DOCUMENTS CAN ONLY BE UPLOADED ONCE");
        }

        validateFileSize(files);
    }

    public void validateFileSize(MultipartFile[] files) {
        if (files.length == 0) {
            throw new ExternalApiValidationException("FILE COUNT MUST NOT BE ZERO");
        }
    }
}
