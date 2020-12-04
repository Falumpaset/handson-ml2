package de.immomio.service.contract;

import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.model.repository.core.shared.contract.signer.aes.BaseDigitalContractItpHistoryRepository;
import de.immomio.service.contract.history.CrawlerDigitalContractHistoryService;
import de.immomio.service.contract.notification.CrawlerDigitalContractStatusNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class DigitalContractItpRetryService {

    private final BaseDigitalContractSignerRepository signerRepository;

    private final BaseDigitalContractItpHistoryRepository itpHistoryRepository;

    private final DigitalContractItpService itpService;

    private final DigitalContractSignerHistoryService signerHistoryService;

    private final CrawlerDigitalContractHistoryService contractHistoryService;

    private final CrawlerDigitalContractStatusNotificationService notificationService;

    @Value("${dmv.itp.failure.maxtries}")
    private Long itpMaxTries;

    @Value("${dmv.itp.failure.warnthreshold}")
    private Long warningThreshold;

    @Value("${dmv.itp.failure.sendmail}")
    private Boolean notify;

    @Autowired
    public DigitalContractItpRetryService(BaseDigitalContractSignerRepository signerRepository, BaseDigitalContractItpHistoryRepository itpHistoryRepository, DigitalContractItpService itpService, DigitalContractSignerHistoryService signerHistoryService, CrawlerDigitalContractHistoryService contractHistoryService, CrawlerDigitalContractStatusNotificationService notificationService) {
        this.signerRepository = signerRepository;
        this.itpHistoryRepository = itpHistoryRepository;
        this.itpService = itpService;
        this.signerHistoryService = signerHistoryService;
        this.contractHistoryService = contractHistoryService;
        this.notificationService = notificationService;
    }

    public void retryFailedItpRequests() {
        List<DigitalContractSigner> signers = signerRepository.findSignersWithAesItpState(List.of(DigitalContractItpState.TECHNICAL_ERROR.name()));
        signers.forEach(signer -> {
            itpService.processItpCheck(signer, signer.getAesVerificationData().getTemporaryIban(), false);

            Long countItpTries = itpHistoryRepository.countBySignerAndState(signer, DigitalContractItpState.TECHNICAL_ERROR);
            if (countItpTries >= itpMaxTries) {
                signerHistoryService.updateSignerHistory(DigitalContractSignerHistoryState.INTERNAL_ITP_CODE_VERIFICATION_FAILED, signer);
                itpService.saveItpHistory(signer, null, DigitalContractItpState.INTERNAL_CANCELED, null, null, null);
                contractHistoryService.historyInternalCanceled(signer.getDigitalContract());
                notificationService.sendItpFailed(signer, signerRepository.findByDigitalContract(signer.getDigitalContract()));
                itpService.saveSignerItpData(signer, null);
                if (notify) {
                    notificationService.sentInternalItpFailed(signer);
                }
            }

            if (countItpTries.equals(warningThreshold)) {
                notificationService.sentInternalItpWarn(signer);
            }
        });
    }
}
