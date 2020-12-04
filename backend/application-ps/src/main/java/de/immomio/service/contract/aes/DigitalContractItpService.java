package de.immomio.service.contract.aes;

import de.immomio.itp.client.ItpClient;
import de.immomio.model.repository.shared.contract.signer.DigitalContractSignerRepository;
import de.immomio.model.repository.shared.contract.signer.aes.DigitalContractItpHistoryRepository;
import de.immomio.service.shared.contract.AbstractDigitalContractItpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */
@Slf4j
@Service
public class DigitalContractItpService extends AbstractDigitalContractItpService<DigitalContractItpHistoryRepository, DigitalContractSignerRepository> {

    @Autowired
    public DigitalContractItpService(
            ItpClient itpClient,
            DigitalContractItpHistoryRepository itpHistoryRepository,
            DigitalContractSignerRepository signerRepository
    ) {
       super(itpHistoryRepository, signerRepository, itpClient);
    }

}
