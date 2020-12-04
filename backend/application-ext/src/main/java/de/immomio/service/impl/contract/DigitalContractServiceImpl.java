package de.immomio.service.impl.contract;

import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.contract.DigitalContractCreateMethod;
import de.immomio.data.base.type.contract.DigitalContractType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.base.type.product.quota.QuotaProductType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.exception.ExternalApiNotFoundException;
import de.immomio.exception.ExternalApiValidationException;
import de.immomio.model.contract.ApiSignatureType;
import de.immomio.model.contract.ContractStatusApiBean;
import de.immomio.model.contract.CreateContractApiBean;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.shared.contract.DigitalContractRepository;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.service.base.contract.DigitalContractService;
import de.immomio.service.impl.UserSecurityService;
import de.immomio.service.impl.quota.LandlordQuotaUsageService;
import de.immomio.service.shared.contract.AbstractDigitalContractService;
import de.immomio.service.shared.contract.DigitalContractDataConverter;
import de.immomio.service.validation.DigitalContractValidationService;
import de.immomio.utils.FileStorageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class DigitalContractServiceImpl extends AbstractDigitalContractService implements DigitalContractService {

    private final UserSecurityService userSecurityService;

    private final DigitalContractDataConverter digitalContractDataConverter;

    private final DigitalContractRepository digitalContractRepository;

    private final DigitalContractSignerRepository digitalContractSignerRepository;

    private final DigitalContractHistoryService digitalContractHistoryService;

    private final DigitalContractMapper digitalContractMapper;

    private final LandlordQuotaUsageService landlordQuotaUsageService;

    private final DigitalContractValidationService validationService;

    private final LandlordS3FileManager s3FileManager;

    private final LandlordUserRepository userRepository;

    @Autowired
    public DigitalContractServiceImpl(
            UserSecurityService userSecurityService,
            DigitalContractDataConverter digitalContractDataConverter,
            DigitalContractRepository digitalContractRepository,
            DigitalContractSignerRepository digitalContractSignerRepository,
            DigitalContractHistoryService digitalContractHistoryService,
            DigitalContractMapper digitalContractMapper,
            LandlordQuotaUsageService landlordQuotaUsageService,
            DigitalContractValidationService validationService,
            LandlordS3FileManager s3FileManager,
            LandlordUserRepository userRepository) {
        this.userSecurityService = userSecurityService;
        this.digitalContractDataConverter = digitalContractDataConverter;
        this.digitalContractRepository = digitalContractRepository;
        this.digitalContractSignerRepository = digitalContractSignerRepository;
        this.digitalContractHistoryService = digitalContractHistoryService;
        this.digitalContractMapper = digitalContractMapper;
        this.landlordQuotaUsageService = landlordQuotaUsageService;
        this.validationService = validationService;
        this.s3FileManager = s3FileManager;
        this.userRepository = userRepository;
    }

    @Override
    public void createDigitalContract(CreateContractApiBean apiBean) {
        validationService.validateContractModuleBooked();
        validationService.validateContractExists(apiBean);
        validationService.validateApiUserExists();
        validationService.validateApiBean(apiBean);

        try {
            landlordQuotaUsageService.validateQuota(QuotaProductType.DIGITAL_CONTRACT);
        } catch (ApiValidationException e) {
            throw new ExternalApiValidationException("QUOTA LIMIT EXCEEDED");
        }

        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        DigitalContractSignerType defaultSignerType = customer.getCustomerSettings().getContractCustomerSettings().getContractDefaultSignerType();

        DigitalContract digitalContract = new DigitalContract();
        digitalContract.setSignatureType(digitalContractMapper.mapSignatureType(apiBean.getSignatureType()));
        digitalContract.setExternalId(apiBean.getId());
        digitalContract.setInternalContractId(UUID.randomUUID());
        digitalContract.setDigitalContractType(DigitalContractType.RENTAL);
        digitalContract.setCustomer(customer);
        digitalContract.setPropertyData(digitalContractMapper.mapPropertyData(apiBean.getProperty()));
        digitalContract.setCreateMethod(DigitalContractCreateMethod.API);

        setOnsiteHostIfNeeded(apiBean, digitalContract);

        if (apiBean.getFirstSigner() != null) {
            digitalContract.setFirstSignerType(DigitalContractSignerType.valueOf(apiBean.getFirstSigner().name()));
        } else {
            digitalContract.setFirstSignerType(defaultSignerType);
        }
        List<DigitalContractSignerData> landlordSignerData = digitalContractMapper.mapSignerData(apiBean.getLandlordSigners());
        List<DigitalContractSigner> landlordSigners = digitalContractDataConverter.convertSignerBeansToEntities(digitalContract, landlordSignerData,
                DigitalContractSignerType.LANDLORD);

        List<DigitalContractSignerData> tenantSignerData = digitalContractMapper.mapSignerData(apiBean.getTenantSigners());
        List<DigitalContractSigner> tenantSigners = digitalContractDataConverter.convertSignerBeansToEntities(digitalContract, tenantSignerData,
                DigitalContractSignerType.TENANT);

        digitalContract = digitalContractRepository.save(digitalContract);
        digitalContractSignerRepository.saveAll(landlordSigners);
        digitalContractSignerRepository.saveAll(tenantSigners);
        digitalContractHistoryService.historyExternalCreated(digitalContract);
    }

    @Override
    public void uploadDigitalContract(String externalId, MultipartFile[] files) {
        validationService.validateContractModuleBooked();
        DigitalContract contract = digitalContractRepository.findFirstByExternalId(externalId).orElseThrow(ExternalApiNotFoundException::new);
        validationService.validateUploadFiles(contract, files);

        List<S3File> s3Files = Arrays.stream(files).map(digitalContractMapper::uploadMultipart).collect(Collectors.toList());
        contract.setDocumentFiles(s3Files);

        digitalContractRepository.save(contract);
    }

    @Override
    public List<String> getFinishedContracts(Date date) {
        validationService.validateContractModuleBooked();
        List<DigitalContract> contract = digitalContractRepository.findFinishedAfter(date);
        return contract.stream().map(DigitalContract::getExternalId).filter(Objects::nonNull).map(String::valueOf).collect(Collectors.toList());
    }

    @Override
    public List<String> getModifiedContracts(Date date) {
        validationService.validateContractModuleBooked();
        List<DigitalContract> contract = digitalContractRepository.findModifiedAfter(date);
        return contract.stream().map(DigitalContract::getExternalId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public ContractStatusApiBean getContractInfo(String id) {
        validationService.validateContractModuleBooked();
        return digitalContractRepository.findFirstByExternalId(id).stream().map(digitalContractMapper::mapStatusBean)
                .findFirst()
                .orElseThrow(() -> new ExternalApiNotFoundException("CONTRACT NOT FOUND"));
    }

    @Override
    public ResponseEntity downloadContract(String id) {
        validationService.validateContractModuleBooked();
        DigitalContract digitalContract = digitalContractRepository.findFirstByExternalId(id).orElseThrow(ExternalApiNotFoundException::new);
        validationService.validateContractDownload(digitalContract);

        try {
            S3File documentArchiveFile = digitalContract.getSignedDocumentArchiveCertificateFile();

            return FileStorageUtils.getFile(documentArchiveFile.getUrl(), documentArchiveFile.getFilename(), s3FileManager, false, null);
        } catch (IOException e) {
            throw new ImmomioRuntimeException();
        }
    }

    private void setOnsiteHostIfNeeded(CreateContractApiBean apiBean, DigitalContract digitalContract) {
        if (apiBean.getSignatureType() == ApiSignatureType.ONSITE) {
            LandlordUser onsiteHostAgent = userRepository.findByEmail(apiBean.getOnsiteHostEmailAddress());
            if (onsiteHostAgent == null || !onsiteHostAgent.getCustomer().equals(userSecurityService.getPrincipalUser().getCustomer())) {
                throw new ExternalApiValidationException("NO USER WITH ONSITE EMAIL FOUND");
            }
            digitalContract.setOnsiteHostAgent(onsiteHostAgent);
        }
    }

}
