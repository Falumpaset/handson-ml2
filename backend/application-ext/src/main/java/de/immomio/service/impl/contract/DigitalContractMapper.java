package de.immomio.service.impl.contract;

import de.immomio.beans.shared.contract.DigitalContractSignerSimpleState;
import de.immomio.beans.shared.contract.DigitalContractSimpleState;
import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.common.upload.FileStoreService;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractPropertyData;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.exception.ExternalApiValidationException;
import de.immomio.model.ApiPropertyInfo;
import de.immomio.model.address.ApiAddressAnswer;
import de.immomio.model.address.ApiPersonAddressAnswer;
import de.immomio.model.address.ApiPropertyAddressAnswer;
import de.immomio.model.contract.ApiContractState;
import de.immomio.model.contract.ApiSignatureType;
import de.immomio.model.contract.ContractStatusApiBean;
import de.immomio.model.contract.SignerInfo;
import de.immomio.model.contract.SignerStatusInfo;
import de.immomio.model.contract.SignerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Component
public class DigitalContractMapper {

    private final LandlordS3FileManager s3FileManager;

    @Autowired
    public DigitalContractMapper(LandlordS3FileManager s3FileManager) {
        this.s3FileManager = s3FileManager;
    }

    public S3File uploadMultipart(MultipartFile multipartFile) {
        FileStoreObject fileStoreObject = FileStoreService.fileStoreObject(multipartFile, FileType.SHARED_DOCUMENT, false);
        File tempFile = null;
        try {
            tempFile = s3FileManager.createTempFile(multipartFile, FileType.SHARED_DOCUMENT, fileStoreObject.getIdentifier(), fileStoreObject.getExtension());
            String url = s3FileManager.uploadFile(tempFile, FileType.SHARED_DOCUMENT, fileStoreObject.getIdentifier(), fileStoreObject.getExtension());
            S3File s3File = new S3File();
            s3File.setType(FileType.SHARED_DOCUMENT);
            s3File.setUrl(url);
            s3File.setIdentifier(fileStoreObject.getIdentifier());
            s3File.setTitle(fileStoreObject.getName());
            s3File.setName(fileStoreObject.getName());
            s3File.setExtension(fileStoreObject.getExtension());
            return s3File;
        } catch (S3FileManagerException e) {
            throw new ImmomioRuntimeException("FAILED TO UPLOAD DOCUMENTS");
        } finally {
            if (tempFile != null) {
                FileUtilities.forceDelete(tempFile);
            }
        }
    }

    public DigitalContractSignatureType mapSignatureType(ApiSignatureType signatureType) {
        if (signatureType == null) {
            throw new ExternalApiValidationException("SIGNATURE TYPE MUST BE SET");
        }

        return switch (signatureType) {
            case ONSITE -> DigitalContractSignatureType.AES_OFFICE;
            case SIMPLE -> DigitalContractSignatureType.ES_MAIL;
            case ADVANCED -> DigitalContractSignatureType.AES_MAIL;
        };
    }

    public DigitalContractPropertyData mapPropertyData(ApiPropertyInfo propertyInfo) {
        Address address = mapAddress(propertyInfo.getAddress());

        DigitalContractPropertyData contractPropertyData = DigitalContractPropertyData.builder()
                .additionalCosts(propertyInfo.getAdditionalCosts())
                .bailment(propertyInfo.getDeposit())
                .externalId(propertyInfo.getId())
                .floor(propertyInfo.getFloor())
                .heatingCost(propertyInfo.getHeatingCost())
                .name(propertyInfo.getName())
                .rent(propertyInfo.getRent())
                .rooms(propertyInfo.getRooms())
                .size(propertyInfo.getSize())
                .address(address)
                .build();
        return contractPropertyData;
    }

    public Address mapAddress(ApiAddressAnswer apiAddressAnswer) {
        Address address = new Address();
        address.setZipCode(apiAddressAnswer.getZipCode());
        address.setHouseNumber(apiAddressAnswer.getHouseNumber());
        address.setStreet(apiAddressAnswer.getStreet());
        address.setCity(apiAddressAnswer.getCity());
        address.setRegion(apiAddressAnswer.getFederalProvince());
        return address;
    }

    public List<DigitalContractSignerData> mapSignerData(List<SignerInfo> signerInfos) {
        return signerInfos.stream().map(signerInfo -> {
            return DigitalContractSignerData.builder()
                    .firstname(signerInfo.getFirstname())
                    .lastname(signerInfo.getLastname())
                    .email(signerInfo.getAddress().getEmail())
                    .address(mapAddress(signerInfo.getAddress()))
                    .gender(signerInfo.getGender())
                    .build();
        }).collect(Collectors.toList());
    }

    public ApiPersonAddressAnswer mapPersonApiAddressAnswer(Address address) {
        return new ApiPersonAddressAnswer(address.getCity(), address.getStreet(), address.getHouseNumber(), address.getZipCode(), address.getRegion(), null);
    }

    public ApiPropertyAddressAnswer mapPropertyApiAddressAnswer(Address address) {
        return new ApiPropertyAddressAnswer(address.getCity(), address.getStreet(), address.getHouseNumber(), address.getZipCode(), address.getRegion());
    }

    public ContractStatusApiBean mapStatusBean(DigitalContract contract) {
        ContractStatusApiBean statusApiBean = new ContractStatusApiBean();
        statusApiBean.setFirstSigner(SignerType.valueOf(contract.getFirstSignerType().name()));
        statusApiBean.setId(contract.getExternalId());
        statusApiBean.setProperty(mapApiPropertyInfo(contract.getPropertyData()));
        statusApiBean.setSignatureType(mapApiSignatureType(contract.getSignatureType()));

        List<SignerStatusInfo> landlordSignerInfos = contract.getSigners().stream()
                .filter(signer -> signer.getType() == DigitalContractSignerType.LANDLORD)
                .map(this::mapSignerStatusInfo).collect(Collectors.toList());

        List<SignerStatusInfo> tenantSignerInfos = contract.getSigners().stream()
                .filter(signer -> signer.getType() == DigitalContractSignerType.TENANT)
                .map(this::mapSignerStatusInfo).collect(Collectors.toList());

        statusApiBean.setLandlordSigners(landlordSignerInfos);
        statusApiBean.setTenantSigners(tenantSignerInfos);

        ApiContractState simpleState = mapContractState(DigitalContractSimpleState.getByContractState(contract.getCurrentState()));
        statusApiBean.setStatus(simpleState);

        return statusApiBean;
    }

    public ApiSignatureType mapApiSignatureType(DigitalContractSignatureType signatureType) {
        return switch (signatureType) {
            case AES_OFFICE -> ApiSignatureType.ONSITE;
            case ES_MAIL -> ApiSignatureType.SIMPLE;
            case AES_MAIL -> ApiSignatureType.ADVANCED;
            default -> null;
        };

    }

    private ApiPropertyInfo mapApiPropertyInfo(DigitalContractPropertyData propertyData) {
        return ApiPropertyInfo.builder()
                .additionalCosts(propertyData.getAdditionalCosts())
                .address(mapPropertyApiAddressAnswer(propertyData.getAddress()))
                .floor(propertyData.getFloor())
                .heatingCost(propertyData.getHeatingCost())
                .name(propertyData.getName())
                .id(propertyData.getExternalId())
                .rooms(propertyData.getRooms())
                .rent(propertyData.getRent())
                .size(propertyData.getSize())
                .deposit(propertyData.getBailment())
                .build();

    }

    private ApiContractState mapContractState(DigitalContractSimpleState simpleState) {
        switch (simpleState) {
            case CANCELED:
                return ApiContractState.CANCELED;
            case FINISHED:
                return ApiContractState.FINISHED;
            case IN_PROGRESS:
            case PROTECTED:
            case CONFIGURATION_NEEDED:
                return ApiContractState.IN_PROGRESS;
            default:
                return null;
        }
    }

    private SignerStatusInfo mapSignerStatusInfo(DigitalContractSigner signer) {
        SignerStatusInfo statusInfo = new SignerStatusInfo();
        statusInfo.setFirstname(signer.getData().getFirstname());
        statusInfo.setLastname(signer.getData().getLastname());
        Address address = signer.getData().getAddress();
        ApiPersonAddressAnswer apiAddressAnswer = mapPersonApiAddressAnswer(address);
        apiAddressAnswer.setEmail(signer.getData().getEmail());
        statusInfo.setAddress(apiAddressAnswer);
        DigitalContractSignerSimpleState simpleState = DigitalContractSignerSimpleState.getByContractState(signer.getCurrentState().getSignerState());
        statusInfo.setStatus(simpleState != null ? simpleState : DigitalContractSignerSimpleState.WAITING);
        statusInfo.setGender(signer.getData().getGender());
        return statusInfo;
    }
}
