package de.immomio.landlord.service.contract;

import de.immomio.beans.shared.contract.DigitalContractDataBean;
import de.immomio.beans.shared.contract.DigitalContractSignBean;
import de.immomio.beans.shared.contract.tenant.token.DigitalContractSigningTokenBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.docusign.service.DocuSignSignerService;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.security.common.bean.DigitalContractSignToken;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.shared.contract.AbstractDigitalContractSignerService;
import de.immomio.service.shared.contract.DigitalContractDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Niklas Lindemann
 */

@Service
public class DigitalContractSignerService extends AbstractDigitalContractSignerService {

    public static final String USER_NOT_FOUND_L = "USER_NOT_FOUND_L";
    private final DigitalContractSignerRepository digitalContractSignerRepository;
    private final DigitalContractDataConverter digitalContractDataConverter;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public DigitalContractSignerService(
            DigitalContractSignerRepository digitalContractSignerRepository,
            DigitalContractDataConverter digitalContractDataConverter,
            DocuSignSignerService docuSignSignerService,
            JWTTokenService jwtTokenService) {
        super(docuSignSignerService);
        this.digitalContractSignerRepository = digitalContractSignerRepository;
        this.digitalContractDataConverter = digitalContractDataConverter;
        this.jwtTokenService = jwtTokenService;

    }

    public DigitalContractEmbeddedSendingBean getSigningUrl(DigitalContractSigningTokenBean tokenBean) {
        DigitalContractSigner signer = getSignerFromToken(tokenBean.getToken());

        return getDocuSignSignerService().getEmbeddedSigningUrl(signer, tokenBean.getRedirectUrl());
    }

    public DigitalContractEmbeddedSendingBean getSigningUrl(DigitalContractSigner signer, String redirectUrl) {
        return getDocuSignSignerService().getEmbeddedSigningUrl(signer, redirectUrl);
    }

    public DigitalContractSignBean getContractSignBean(String token) {
        DigitalContractSigner signer = getSignerFromToken(token);
        return getContractSignBean(signer);
    }

    public DigitalContractSigner getSignerFromToken(String token) {
        DigitalContractSignToken contractSignToken = jwtTokenService.validateContractSignToken(token);
        return digitalContractSignerRepository.findById(contractSignToken.getSignerId()).orElseThrow(() -> new ApiValidationException("SIGNER_NOT_FOUND_L"));
    }

    public S3File getSignedContract(Long signerId, String s3Identifier) {
        DigitalContractSigner signer = digitalContractSignerRepository.findById(signerId).orElseThrow(() -> new ApiValidationException("SIGNER_NOT_FOUND_L"));
        DigitalContract digitalContract = signer.getDigitalContract();
        S3File archiveFile = digitalContract.getSignedDocumentArchiveFile();
        if (archiveFile != null && s3Identifier.equals(archiveFile.getIdentifier())) {
            return archiveFile;
        }
        S3File archiveCertificateFile = digitalContract.getSignedDocumentArchiveCertificateFile();
        if (archiveCertificateFile != null && s3Identifier.equals(archiveCertificateFile.getIdentifier())) {
            return archiveCertificateFile;
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
                    .orElseThrow(() -> new ApiValidationException("CONTRACT_NOT_SIGNED_YET_L"));
        }

        throw new ApiValidationException("CONTRACT_NOT_SIGNED_YET_L");
    }

    public List<DigitalContractSigner> mergeSignersIntoNewContract(final DigitalContract digitalContract, final DigitalContractDataBean contractBean) {
        List<DigitalContractSigner> digitalContractSigners =
                digitalContractDataConverter.convertSignerBeansToEntities(
                        digitalContract,
                        contractBean.getLandlordSigners(),
                        DigitalContractSignerType.LANDLORD
                );
        digitalContractSigners.addAll(
                digitalContractDataConverter.convertSignerBeansToEntities(
                        digitalContract,
                        contractBean.getTenantSigners(),
                        DigitalContractSignerType.TENANT
                )
        );
        digitalContractSignerRepository.saveAll(digitalContractSigners);

        return digitalContractSigners;
    }

    public List<DigitalContractSigner> mergeSignersIntoExistingContract(final DigitalContract digitalContract, final DigitalContractDataBean contractBean) {
        //this is only a temporary solution until we implemented the "real" update flow
        digitalContractSignerRepository.deleteAll(digitalContract.getSigners());
        digitalContract.setSigners(new ArrayList<>());

        List<DigitalContractSigner> digitalContractSigners =
                digitalContractDataConverter.convertSignerBeansToEntities(
                        digitalContract,
                        contractBean.getLandlordSigners(),
                        DigitalContractSignerType.LANDLORD
                );
        digitalContractSigners.addAll(
                digitalContractDataConverter.convertSignerBeansToEntities(
                        digitalContract,
                        contractBean.getTenantSigners(),
                        DigitalContractSignerType.TENANT
                )
        );
        digitalContractSignerRepository.saveAll(digitalContractSigners);

        return digitalContractSigners;
    }

    public List<DigitalContractSigner> saveAllSigners(final List<DigitalContractSigner> digitalContractSigners) {
        return digitalContractSignerRepository.saveAll(digitalContractSigners);
    }

    public DigitalContractSigner findSignerByInternalId(UUID internalId) {
        return digitalContractSignerRepository.findByInternalSignerId(internalId).orElseThrow();
    }

}
