package de.immomio.service.shared.contract;

import de.immomio.beans.shared.contract.DigitalContractDataBean;
import de.immomio.data.base.type.contract.DigitalContractCreateMethod;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.DigitalContractType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.contract.DigitalContractSignerAesVerificationData;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DigitalContractDataConverter {

    private static final String ITP_CODE_PATTERN = "(?=.*[0-9])(?=.*[a-z])([a-z0-9]+)";
    private static final String ITP_DEFAULT_CODE = "c11zy030";

    public DigitalContract convertContractBeanToEntity(DigitalContractDataBean contract, LandlordUser user) {
        DigitalContract digitalContract = new DigitalContract();
        digitalContract.setInternalContractId(UUID.randomUUID());
        digitalContract.setCustomer(user.getCustomer());
        digitalContract.setAgentInfo(new AgentInfo(user));
        digitalContract.setSignatureType(contract.getSignatureType());
        digitalContract.setDocumentFiles(contract.getDocumentFiles());
        digitalContract.setFirstSignerType(getSignerType(user));
        digitalContract.setDigitalContractType(DigitalContractType.RENTAL);
        digitalContract.setPropertyData(contract.getProperty());
        digitalContract.setCreateMethod(DigitalContractCreateMethod.APP);

        return digitalContract;
    }

    public List<DigitalContractSigner> convertSignerBeansToEntities(
            final DigitalContract digitalContract,
            final List<DigitalContractSignerData> signerBeans,
            final DigitalContractSignerType type
    ) {
        List<DigitalContractSigner> signerEntities =
                signerBeans
                        .stream()
                        .map(signerData -> {
                            DigitalContractSigner signer = new DigitalContractSigner();
                            signer.setType(type);
                            signer.setDigitalContract(digitalContract);
                            signer.setData(signerData);
                            signer.setInternalSignerId(UUID.randomUUID());
                            signer.setOnsiteHost(signerData.isOnsiteHost());
                            signerData.setInternalSignerId(signer.getInternalSignerId());
                            if (signerData.getDocuSignRecipientClientId() != null) {
                                signer.setDocuSignRecipientClientId(signerData.getDocuSignRecipientClientId());
                            } else {
                                signer.setDocuSignRecipientClientId(UUID.randomUUID());
                                signerData.setDocuSignRecipientClientId(signer.getDocuSignRecipientClientId());
                            }
                            if (((digitalContract.getSignatureType() == DigitalContractSignatureType.AES_MAIL ||
                                    digitalContract.getSignatureType() == DigitalContractSignatureType.AES_OFFICE) &&
                                    signer.getType() == DigitalContractSignerType.TENANT)) {
                                DigitalContractSignerAesVerificationData verificationData =
                                        new DigitalContractSignerAesVerificationData();
                                verificationData.setAesCode(generateAesCode());
                                signer.setAesVerificationData(verificationData);
                            }

                            return signer;
                        })
                        .collect(Collectors.toList());

        return signerEntities;
    }

    public List<DigitalContractSigner> mergeSignerBeansToEntities(
            final DigitalContract digitalContract,
            final List<DigitalContractSignerData> signerBeans,
            final DigitalContractSignerType type
    ) {
        List<DigitalContractSigner> existingSigners = digitalContract.getSigners();
        List<DigitalContractSigner> updatedSigners =
                signerBeans
                        .stream()
                        .map(signerData -> {
                            UUID docuSignRecipientClientId = signerData.getDocuSignRecipientClientId();
                            DigitalContractSigner signer = existingSigners
                                    .stream()
                                    .filter(sgn -> sgn.getDocuSignRecipientClientId().equals(docuSignRecipientClientId))
                                    .findAny()
                                    .orElseGet(DigitalContractSigner::new);
                            signer.setType(type);
                            signer.setDigitalContract(digitalContract);
                            signer.setData(signerData);
                            signer.setOnsiteHost(signerData.isOnsiteHost());
                            if (docuSignRecipientClientId != null) {
                                signer.setDocuSignRecipientClientId(docuSignRecipientClientId);
                            } else {
                                signer.setDocuSignRecipientClientId(UUID.randomUUID());
                                signerData.setDocuSignRecipientClientId(signer.getDocuSignRecipientClientId());
                            }

                            return signer;
                        })
                        .collect(Collectors.toList());

        return updatedSigners;
    }

    public DigitalContractDataBean convertContractEntityToBean(DigitalContract digitalContract) {
        DigitalContractDataBean.DigitalContractDataBeanBuilder contractBuilder = DigitalContractDataBean
                .builder()
                .applicationId((digitalContract.getApplication() != null)
                        ? digitalContract.getApplication().getId()
                        : null)
                .internalContractId(digitalContract.getInternalContractId())
                .documentFiles(digitalContract.getDocumentFiles())
                .firstSigner(digitalContract.getFirstSignerType())
                .signatureType(digitalContract.getSignatureType())
                .actionRequired(digitalContract.getCountTenantsStoppedProcess() > 0L)
                .newEnvelopeNeeded(digitalContract.getCountSignersCompleted() > 0L)
                .signedDocumentCombinedFile(digitalContract.getSignedDocumentCombinedFile())
                .signedDocumentSingleFiles(digitalContract.getSignedDocumentSingleFiles())
                .signedDocumentArchiveFile(digitalContract.getSignedDocumentArchiveCertificateFile())
                .property(digitalContract.getPropertyData());

        contractBuilder.landlordSigners(convertSignerEntitiesToBeans(
                digitalContract.getSigners(),
                DigitalContractSignerType.LANDLORD)
        );

        contractBuilder.tenantSigners(convertSignerEntitiesToBeans(
                digitalContract.getSigners(),
                DigitalContractSignerType.TENANT)
        );

        return contractBuilder.build();
    }

    public List<DigitalContractSignerData> convertSignerEntitiesToBeans(
            List<DigitalContractSigner> signers,
            DigitalContractSignerType signerType
    ) {
        return signers
                .stream()
                .filter(signer -> signer.getType() == signerType)
                .map(signer -> {
                    DigitalContractSignerData signerData = signer.getData();
                    signerData.setCurrentState(signer.getCurrentState());
                    return signerData;
                })
                .collect(Collectors.toList());
    }

    public DigitalContract mergeDigitalContract(
            DigitalContract digitalContract,
            DigitalContractDataBean updatedContract
    ) {
        digitalContract.setSignatureType(updatedContract.getSignatureType());
        digitalContract.setDocumentFiles(updatedContract.getDocumentFiles());
        digitalContract.setDigitalContractType(DigitalContractType.RENTAL);
        digitalContract.setPropertyData(updatedContract.getProperty());

        return digitalContract;
    }

    private DigitalContractSignerType getSignerType(LandlordUser user) {
        LandlordCustomerSettings customerSettings = user.getCustomer().getCustomerSettings();
        if (customerSettings != null
                && customerSettings.getContractCustomerSettings() != null
                && customerSettings.getContractCustomerSettings().getContractDefaultSignerType() != null) {
            return customerSettings.getContractCustomerSettings().getContractDefaultSignerType();
        }
        return DigitalContractSignerType.TENANT;
    }

    private String generateAesCode() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .selectFrom("abcdefghjkmnpqrstuvwxyz123456789".toCharArray())
                .build();
        // loop until a matching code is found, counter to 1000 is only to avoid an endless loop
        for (int i = 0; i < 1000; i++) {
            String generatedStr = generator.generate(8);
            if (generatedStr.matches(ITP_CODE_PATTERN)) {
                return generatedStr;
            }
        }

        return ITP_DEFAULT_CODE;
    }

}
