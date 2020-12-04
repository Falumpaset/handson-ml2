package de.immomio.service.base.contract;

import de.immomio.model.contract.ContractStatusApiBean;
import de.immomio.model.contract.CreateContractApiBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
public interface DigitalContractService {
    void createDigitalContract(CreateContractApiBean apiBean);

    void uploadDigitalContract(String externalId, MultipartFile[] files);

    List<String> getFinishedContracts(Date date);

    List<String> getModifiedContracts(Date date);

    ContractStatusApiBean getContractInfo(String id);

    ResponseEntity downloadContract(String id);
}
