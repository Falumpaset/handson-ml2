package de.immomio.service.contract;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.service.contract.history.CrawlerDigitalContractSignerHistoryService;
import de.immomio.service.contract.notification.CrawlerDigitalContractStatusNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.ACCEPTED;
import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.FAILED;
import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.UPLOADED;

@Slf4j
@Service
public class DigitalContractItpSignerStatusCheckService {

    private static final List<String> ITP_STATES_FINISHED = List.of(
            UPLOADED.toString(), ACCEPTED.toString());

    private final BaseDigitalContractSignerRepository contractSignerRepository;
    private final CrawlerDigitalContractSignerHistoryService crawlerContractSignerHistoryService;
    private final CrawlerDigitalContractStatusNotificationService notificationService;

    @Autowired
    public DigitalContractItpSignerStatusCheckService(
            BaseDigitalContractSignerRepository contractSignerRepository,
            CrawlerDigitalContractSignerHistoryService crawlerContractSignerHistoryService,
            CrawlerDigitalContractStatusNotificationService notificationService
    ) {
        this.contractSignerRepository = contractSignerRepository;
        this.crawlerContractSignerHistoryService = crawlerContractSignerHistoryService;
        this.notificationService = notificationService;
    }

    public void handleItpSignerNotification() {
        List<DigitalContractSigner> signersFinished =
                contractSignerRepository.findSignersWithAesItpState(ITP_STATES_FINISHED);
        if (signersFinished != null) {
            signersFinished.forEach(this::notifySignerFinished);
        }
        List<DigitalContractSigner> signersFailed =
                contractSignerRepository.findSignersWithAesItpState(List.of(FAILED.toString()));
        if (signersFailed != null) {
            signersFailed.forEach(this::notifySignerFailed);
        }
    }

    private void notifySignerFinished(DigitalContractSigner signer) {
        DigitalContractSignerHistoryState currentHistoryState = signer.getCurrentState().getSignerState();
        if (currentHistoryState == null || (currentHistoryState.getLevel() < DigitalContractSignerHistoryState.INTERNAL_ITP_MAIL_SENT.getLevel())) {
            try {
                notificationService.sendAesTokenReadyPs(signer);
                crawlerContractSignerHistoryService.signerHistoryItpMailSent(signer);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void notifySignerFailed(DigitalContractSigner signer) {
        DigitalContractSignerHistoryState currentHistoryState = signer.getCurrentState().getSignerState();
        if (currentHistoryState.getLevel() < DigitalContractSignerHistoryState.INTERNAL_ITP_MAIL_SENT.getLevel()) {
            try {
                notificationService.sendAesTokenFailedPs(signer);
                crawlerContractSignerHistoryService.signerHistoryItpMailSent(signer);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
