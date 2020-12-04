package de.immomio.service.mock.contract;

import de.immomio.beans.shared.contract.DigitalContractSignerSimpleState;
import de.immomio.exception.ExternalApiContractNotFinishedException;
import de.immomio.exception.ExternalApiNotFoundException;
import de.immomio.model.ApiPropertyInfo;
import de.immomio.model.address.ApiPersonAddressAnswer;
import de.immomio.model.address.ApiPropertyAddressAnswer;
import de.immomio.model.contract.ApiContractState;
import de.immomio.model.contract.ApiSignatureType;
import de.immomio.model.contract.ContractStatusApiBean;
import de.immomio.model.contract.CreateContractApiBean;
import de.immomio.model.contract.SignerStatusInfo;
import de.immomio.model.contract.SignerType;
import de.immomio.service.base.contract.DigitalContractService;
import de.immomio.service.validation.DigitalContractValidationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Service
public class DigitalContractServiceMock implements DigitalContractService {
    private final DigitalContractValidationService validationService;

    private static final List<String> VALID_IDS = Arrays.asList("1", "2");

    private static final String NOT_FINISHED_ID = "2";

    @Autowired
    public DigitalContractServiceMock(DigitalContractValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public void createDigitalContract(CreateContractApiBean apiBean) {
        validationService.validateApiBean(apiBean);
    }

    @Override
    public void uploadDigitalContract(String externalId, MultipartFile[] files) {
        if (!VALID_IDS.contains(externalId)) {
            throw new ExternalApiNotFoundException();
        }
        validationService.validateFileSize(files);
    }

    @Override
    public List<String> getFinishedContracts(Date date) {
        return VALID_IDS;
    }

    @Override
    public List<String> getModifiedContracts(Date date) {
        return VALID_IDS;
    }

    @Override
    public ContractStatusApiBean getContractInfo(String id) {
        if (!VALID_IDS.contains(id)) {
            throw new ExternalApiNotFoundException();
        }
        ContractStatusApiBean statusApiBean = new ContractStatusApiBean();
        statusApiBean.setId(id);
        statusApiBean.setStatus(ApiContractState.IN_PROGRESS);
        statusApiBean.setSignatureType(ApiSignatureType.ADVANCED);
        statusApiBean.setFirstSigner(SignerType.TENANT);

        SignerStatusInfo tenantSigner = new SignerStatusInfo();
        tenantSigner.setFirstname("Klaus");
        tenantSigner.setFirstname("Sommer");
        ApiPersonAddressAnswer tenantAddress = new ApiPersonAddressAnswer("Großwell", "Marseiller Strasse", "32", "82439", "Bayern", "klaus.sommer@mail.de");
        tenantSigner.setAddress(tenantAddress);
        tenantSigner.setStatus(DigitalContractSignerSimpleState.SIGNED);

        statusApiBean.getTenantSigners().add(tenantSigner);

        SignerStatusInfo landlordSigner = new SignerStatusInfo();
        landlordSigner.setFirstname("Andreas");
        landlordSigner.setFirstname("Kuhn");
        ApiPersonAddressAnswer landlordAddress = new ApiPersonAddressAnswer("Ingolstadt", "Albrechtstrasse", "86", "85022", "Bayern", "albrecht.kuhn@mail.de");
        landlordSigner.setAddress(landlordAddress);
        landlordSigner.setStatus(DigitalContractSignerSimpleState.SIGNED);
        statusApiBean.getLandlordSigners().add(landlordSigner);

        ApiPropertyAddressAnswer apiPropertyAddressAnswer = new ApiPropertyAddressAnswer("Kiel", "Genterstrasse", "75", "24123", "Schlewig Holstein");

        ApiPropertyInfo propertyInfo = ApiPropertyInfo.builder().name("2 Zimmer Wohnung").address(apiPropertyAddressAnswer).id("11-22-33").deposit(3000d).floor(1).rent(1000d).size(70d).rooms(3d).heatingCost(60d).additionalCosts(50d).build();
        statusApiBean.setProperty(propertyInfo);
        return statusApiBean;
    }

    @SneakyThrows
    @Override
    public ResponseEntity downloadContract(String id) {
        if (id.equals(NOT_FINISHED_ID)) {
            throw new ExternalApiContractNotFinishedException();
        }
        InputStream resourceAsStream = getClass().getResourceAsStream("/mock/vertrag.pdf.zip");

        byte[] zipContent =  resourceAsStream.readAllBytes();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract.zip");

        return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);

    }
}
