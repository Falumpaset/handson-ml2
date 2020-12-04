package de.immomio.controller.contract;

import de.immomio.beans.TokenBean;
import de.immomio.beans.shared.contract.DigitalContractDataBean;
import de.immomio.beans.shared.contract.DigitalContractSignBean;
import de.immomio.beans.shared.contract.DigitalContractSignatureMappingBean;
import de.immomio.beans.shared.contract.overview.DigitalContractOverviewFilterBean;
import de.immomio.beans.shared.contract.overview.DigitalContractOverviewItemBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractSigningTokenBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.landlord.service.contract.DigitalContractDeleteService;
import de.immomio.landlord.service.contract.DigitalContractOverviewService;
import de.immomio.landlord.service.contract.DigitalContractService;
import de.immomio.landlord.service.contract.DigitalContractSignerService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.utils.EmailAddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author Andreas Hansen
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "dmv")
public class DigitalContractController {

    private static final String LANDLORD_SIGNERS_MAY_NOT_BE_NULL_L = "LANDLORD_SIGNERS_MAY_NOT_BE_NULL_L";
    private static final String TENANT_SIGNERS_MAY_NOT_BE_NULL_L = "TENANT_SIGNERS_MAY_NOT_BE_NULL_L";
    private static final String PROPERTY_MAY_NOT_BE_NULL_L = "PROPERTY_MAY_NOT_BE_NULL_L";
    private static final String DOCUMENT_FILES_MAY_NOT_BE_NULL_L = "DOCUMENT_FILES_MAY_NOT_BE_NULL_L";
    private static final String TENANT_MUST_NOT_BE_ONSITE_HOST_L = "TENANT_MUST_NOT_BE_ONSITE_HOST_L";
    private static final String SIGNATURE_TYPE_MUST_BE_AES_OFFICE_L = "SIGNATURE_TYPE_MUST_BE_AES_OFFICE_L";
    private static final String EXACT_ONE_ONSITE_HOST_MUST_BE_SET_L = "EXACT_ONE_ONSITE_HOST_MUST_BE_SET_L";
    private static final String ONSITE_HOST_AGENT_MUST_BE_SET_L = "ONSITE_HOST_AGENT_MUST_BE_SET_L";
    private static final String SIGNER_EMAILS_MUST_BE_FILLED_L = "SIGNER_EMAILS_MUST_BE_FILLED_L";
    private static final String SIGNER_EMAILS_MUST_BE_VALID_L = "SIGNER_EMAILS_MUST_BE_VALID_L";

    private final DigitalContractService digitalContractService;
    private final DigitalContractDeleteService digitalContractDeleteContract;
    private final DigitalContractOverviewService digitalContractOverviewService;
    private final DigitalContractSignerService digitalContractSignerService;
    private final UserSecurityService userSecurityService;

    @Autowired
    public DigitalContractController(
            DigitalContractService digitalContractService,
            DigitalContractDeleteService digitalContractDeleteContract,
            DigitalContractOverviewService digitalContractOverviewService,
            DigitalContractSignerService digitalContractSignerService,
            UserSecurityService userSecurityService
    ) {
        this.digitalContractService = digitalContractService;
        this.digitalContractDeleteContract = digitalContractDeleteContract;
        this.digitalContractOverviewService = digitalContractOverviewService;
        this.digitalContractSignerService = digitalContractSignerService;
        this.userSecurityService = userSecurityService;
    }

    @PostMapping("/search/list")
    public ResponseEntity<PagedModel<EntityModel<DigitalContractOverviewItemBean>>> overview(
            @RequestBody DigitalContractOverviewFilterBean filterBean,
            PagedResourcesAssembler<DigitalContractOverviewItemBean> pagedResourcesAssembler
    ) {
        Sort sort = filterBean.getSort() != null ? filterBean.getSort() : Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(filterBean.getPage(), filterBean.getSize(), sort);
        Page<DigitalContractOverviewItemBean> result =
                digitalContractOverviewService.getDigitalContracts(filterBean, pageRequest);

        PagedModel<EntityModel<DigitalContractOverviewItemBean>> body = pagedResourcesAssembler.toModel(result);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{internalContractId}/contract")
    public ResponseEntity<DigitalContractDataBean> contract(@PathVariable("internalContractId") UUID internalContractId) {
        return ResponseEntity.ok(digitalContractService.getContractBeanByInternalContractId(internalContractId));
    }

    @PostMapping("/create")
    public ResponseEntity<DigitalContractEmbeddedSendingBean> create(@RequestBody DigitalContractDataBean contract) {
        validateDigitalContractDataBean(contract);
        DigitalContractEmbeddedSendingBean response = digitalContractService.createDigitalContract(contract);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<DigitalContractEmbeddedSendingBean> update(
            @RequestBody DigitalContractDataBean contract
    ) {
        validateDigitalContractDataBean(contract);
        DigitalContractEmbeddedSendingBean response = digitalContractService.updateDigitalContract(contract);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{internalContractId}/contract")
    public ResponseEntity<DigitalContractDataBean> deleteContract(@PathVariable("internalContractId") UUID internalContractId) {
        digitalContractDeleteContract.deleteContract(internalContractId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signatureMappingUrl")
    public ResponseEntity<DigitalContractEmbeddedSendingBean> signatureMappingUrl(
            @RequestBody DigitalContractSignatureMappingBean digitalContractSignatureMappingBean
    ) {

        return ResponseEntity.ok(digitalContractService.getSignatureMappingUrl(digitalContractSignatureMappingBean));
    }

    @PostMapping("/signingUrl")
    public ResponseEntity<DigitalContractEmbeddedSendingBean> getSignUrl(@RequestBody DigitalContractSigningTokenBean tokenBean) {
        DigitalContractEmbeddedSendingBean sendingBean = digitalContractSignerService.getSigningUrl(tokenBean);
        return ResponseEntity.ok(sendingBean);
    }

    @PostMapping("/getContractData")
    public ResponseEntity<DigitalContractSignBean> getContractData(@RequestBody TokenBean tokenBean) {
        DigitalContractSignBean contractSignBean = digitalContractSignerService.getContractSignBean(tokenBean.getToken());
        return ResponseEntity.ok(contractSignBean);
    }

    @PostMapping("/resendSignMail")
    public ResponseEntity<String> resendSignMail(@RequestParam("internalSignerId") UUID internalId) {
        LandlordUser principalUser = userSecurityService.getPrincipalUser();

        digitalContractService.resendSignMail(internalId, principalUser);
        return ResponseEntity.ok().build();
    }

    private void validateDigitalContractDataBean(DigitalContractDataBean digitalContractDataBean) {
        Assert.notNull(digitalContractDataBean.getLandlordSigners(), LANDLORD_SIGNERS_MAY_NOT_BE_NULL_L);
        Assert.notNull(digitalContractDataBean.getTenantSigners(), TENANT_SIGNERS_MAY_NOT_BE_NULL_L);
        Assert.notNull(digitalContractDataBean.getProperty(), PROPERTY_MAY_NOT_BE_NULL_L);
        Assert.notNull(digitalContractDataBean.getDocumentFiles(), DOCUMENT_FILES_MAY_NOT_BE_NULL_L);

        validateSignerEmails(digitalContractDataBean);

        validateContractSignatureType(digitalContractDataBean);
    }

    private void validateSignerEmails(DigitalContractDataBean digitalContractDataBean) {
        boolean landlordMailEmpty = digitalContractDataBean.getLandlordSigners().stream()
                .anyMatch(signerInfo -> StringUtils.isBlank(signerInfo.getEmail()));

        boolean tenantMailEmpty = digitalContractDataBean.getTenantSigners().stream()
                .anyMatch(signerInfo -> StringUtils.isBlank(signerInfo.getEmail()));

        if (landlordMailEmpty || tenantMailEmpty) {
            throw new ApiValidationException(SIGNER_EMAILS_MUST_BE_FILLED_L);
        }


        boolean landlordMailNotValid = digitalContractDataBean.getLandlordSigners().stream().anyMatch(signerInfo -> EmailAddressUtils.isInvalid(signerInfo.getEmail()));
        boolean tenantMailNotValid = digitalContractDataBean.getTenantSigners().stream().anyMatch(signerInfo -> EmailAddressUtils.isInvalid(signerInfo.getEmail()));
        if (landlordMailNotValid || tenantMailNotValid) {
            throw new ApiValidationException(SIGNER_EMAILS_MUST_BE_VALID_L);
        }
    }

    private void validateContractSignatureType(DigitalContractDataBean digitalContractDataBean) {
        boolean tenantOnsiteSigner = digitalContractDataBean.getTenantSigners().stream().anyMatch(DigitalContractSignerData::isOnsiteHost);
        DigitalContractSignatureType contractSignatureType = digitalContractDataBean.getSignatureType();

        if (tenantOnsiteSigner) {
            throw new ApiValidationException(TENANT_MUST_NOT_BE_ONSITE_HOST_L);
        }

        long onsiteHostCount = digitalContractDataBean.getLandlordSigners().stream().filter(DigitalContractSignerData::isOnsiteHost).count();

        if (contractSignatureType != DigitalContractSignatureType.AES_OFFICE && onsiteHostCount > 0) {
            throw new ApiValidationException(SIGNATURE_TYPE_MUST_BE_AES_OFFICE_L);
        }
        if (contractSignatureType == DigitalContractSignatureType.AES_OFFICE && onsiteHostCount != 1) {
            throw new ApiValidationException(EXACT_ONE_ONSITE_HOST_MUST_BE_SET_L);
        }
        if (contractSignatureType == DigitalContractSignatureType.AES_OFFICE && digitalContractDataBean.getOnsiteHostAgentId() == null) {
            throw new ApiValidationException(ONSITE_HOST_AGENT_MUST_BE_SET_L);
        }
    }

}
