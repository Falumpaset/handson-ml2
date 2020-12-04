package de.immomio.service.shared.contract;

import de.immomio.beans.shared.contract.DigitalContractSignBean;
import de.immomio.beans.shared.contract.DigitalContractSignContactBean;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractCustomerSettings;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractContactInfo;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.beans.DocuSignAdditionalDocumentType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static de.immomio.service.shared.contract.DigitalContractFilenameHelper.PDF;
import static de.immomio.service.shared.contract.DigitalContractFilenameHelper.UNDERSCORE;
import static de.immomio.service.shared.contract.DigitalContractFilenameHelper.append;
import static de.immomio.service.shared.contract.DigitalContractFilenameHelper.finalizeName;
import static de.immomio.service.shared.contract.DigitalContractFilenameHelper.getContractFilenameWithoutEnding;

/**
 * @author Niklas Lindemann
 */
@Slf4j
public abstract class AbstractDigitalContractSignerService {

    private static final String IDENTIFIER_SUFFIX_CERTIFICATE = "_certificate";

    @Getter
    private final DocuSignSignerService docuSignSignerService;

    public AbstractDigitalContractSignerService(DocuSignSignerService docuSignSignerService) {
        this.docuSignSignerService = docuSignSignerService;
    }

    protected void updateViewingDateCombinedDocument(DigitalContractSigner signer, Date flatVisited) {
        if (hasFlatVisited(flatVisited)) {
            docuSignSignerService.updateViewingDateCombinedDocument(signer, DocuSignAdditionalDocumentType.VIEWING_DATE);
        } else {
            docuSignSignerService.updateViewingDateCombinedDocument(signer, DocuSignAdditionalDocumentType.NOT_VIEWED_DISCLAIMER);
        }
    }

    protected void updateIdentityConfirmedDocument(DigitalContractSigner onsiteHostSigner, DigitalContractSigner signer) {
        docuSignSignerService.updateIdentityConfirmedDocument(onsiteHostSigner, signer);
    }

    protected DigitalContractSignBean getContractSignBean(DigitalContractSigner signer) {
        DigitalContract digitalContract = signer.getDigitalContract();

        LandlordCustomerSettings customerSettings = digitalContract.getCustomer().getCustomerSettings();
        DigitalContractSignBean signBean = createContractSignBean(signer);
        if (customerSettings != null) {
            DigitalContractCustomerSettings contractCustomerSettings = customerSettings.getContractCustomerSettings();
            if (contractCustomerSettings != null) {
                signBean.setContinueContractWhenNotVisitedFlat(contractCustomerSettings.getContinueContractWhenNotVisitedFlat());
                DigitalContractContactInfo contractContactInfo = contractCustomerSettings.getContractContactInfo();
                DigitalContractSignContactBean signContactBean = getContractTenantSignContactBean(contractContactInfo);
                signBean.setContactInfo(signContactBean);
            }
        }
        return signBean;
    }

    public String getContractFilename(DigitalContractSigner signer) {
        return getContractFilenameForSigner(signer) + PDF;
    }

    private DigitalContractSignContactBean getContractTenantSignContactBean(DigitalContractContactInfo contractContactInfo) {

        return contractContactInfo != null ? DigitalContractSignContactBean
                .builder()
                .email(contractContactInfo.getEmail())
                .firstname(contractContactInfo.getFirstname())
                .lastname(contractContactInfo.getLastname())
                .phone(contractContactInfo.getPhone())
                .build() : DigitalContractSignContactBean.builder().build();
    }

    private DigitalContractSignBean createContractSignBean(DigitalContractSigner signer) {
        DigitalContract digitalContract = signer.getDigitalContract();

        List<S3File> signedDocumentSingleFiles = digitalContract.getSignedDocumentSingleFiles();
        S3File archiveFile = digitalContract.getSignedDocumentArchiveCertificateFile();
        if (signer.getType() == DigitalContractSignerType.TENANT && signedDocumentSingleFiles != null) {
            signedDocumentSingleFiles = filterSignedDocumentSingleFiles(signedDocumentSingleFiles);
            archiveFile = digitalContract.getSignedDocumentArchiveFile();
        }

        return DigitalContractSignBean
                .builder()
                .signatureType(digitalContract.getSignatureType())
                .internalContractId(digitalContract.getInternalContractId())
                .contractType(digitalContract.getDigitalContractType())
                .propertyData(digitalContract.getPropertyData())
                .signatureType(digitalContract.getSignatureType())
                .signer(Collections.singletonList(signer.getData()))
                .actionRequired(digitalContract.getCountTenantsStoppedProcess() > 0L)
                .signedDocumentCombinedFile(digitalContract.getSignedDocumentCombinedFile())
                .signedDocumentSingleFiles(signedDocumentSingleFiles)
                .signedDocumentArchiveFile(archiveFile)
                .build();
    }

    private String getContractFilenameForSigner(DigitalContractSigner signer) {
        String filename = getContractFilenameWithoutEnding(signer.getDigitalContract()) + UNDERSCORE;
        StringBuilder stringBuilder = new StringBuilder(filename);
        append(stringBuilder, signer.getData().getLastname());
        String fileName = stringBuilder.toString();

        return finalizeName(fileName);
    }

    private List<S3File> filterSignedDocumentSingleFiles(final List<S3File> singleFiles) {
        return singleFiles
                .stream()
                .filter(s3File -> !s3File.getIdentifier().endsWith(IDENTIFIER_SUFFIX_CERTIFICATE))
                .collect(Collectors.toList());
    }

    private boolean hasFlatVisited(Date flatVisited) {
        return flatVisited != null;
    }

}
