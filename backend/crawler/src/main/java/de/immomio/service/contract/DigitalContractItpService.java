package de.immomio.service.contract;

import de.immomio.itp.client.ItpClient;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.model.repository.core.shared.contract.signer.aes.BaseDigitalContractItpHistoryRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractItpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */
@Slf4j
@Service
public class DigitalContractItpService extends AbstractDigitalContractItpService<BaseDigitalContractItpHistoryRepository, BaseDigitalContractSignerRepository> {

    @Autowired
    public DigitalContractItpService(
            ItpClient itpClient,
            BaseDigitalContractItpHistoryRepository itpHistoryRepository,
            BaseDigitalContractSignerRepository signerRepository
    ) {
       super(itpHistoryRepository, signerRepository, itpClient);
    }

}
