package de.immomio.service.contract;

import de.immomio.data.propertysearcher.entity.itp.ItpCheckResponseBean;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpHistory;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import de.immomio.data.shared.entity.contract.signer.history.aes.itp.ItpMaskedRequestBean;
import de.immomio.itp.client.ItpClient;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;
import de.immomio.model.repository.core.shared.contract.signer.aes.BaseDigitalContractItpHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.INIT;
import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.PENDING;
import static de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState.UPLOADED;

@Slf4j
@Service
public class DigitalContractItpStatusService {

    private static final List<String> ITP_STATES_IN_PROGRESS = List.of(
            INIT.toString(), UPLOADED.toString(), PENDING.toString());
    private static final Map<String, List<String>> ITP_MOCK_UUIDS = Map.of(
            "101", List.of("102", "103", "104", "105"),
            "102", List.of("103", "104", "105"),
            "103", List.of("104", "105"),
            "104", List.of("105")
    );

    private final ItpClient itpClient;
    private final BaseDigitalContractSignerRepository contractSignerRepository;
    private final BaseDigitalContractItpHistoryRepository itpHistoryRepository;

    @Autowired
    public DigitalContractItpStatusService(
            ItpClient itpClient,
            BaseDigitalContractSignerRepository contractSignerRepository,
            BaseDigitalContractItpHistoryRepository itpHistoryRepository
    ) {
        this.itpClient = itpClient;
        this.contractSignerRepository = contractSignerRepository;
        this.itpHistoryRepository = itpHistoryRepository;
    }

    public void handleItpStatusChanges() {
        List<DigitalContractSigner> signers =
                contractSignerRepository.findSignersWithAesItpState(ITP_STATES_IN_PROGRESS);
        if (signers != null) {
            signers.forEach(this::identCheckStatus);
        }
    }

    private void identCheckStatus(DigitalContractSigner signer) {
        String identcheckUuid = signer.getAesVerificationData().getIdentcheckUuid();
        log.info("Checking ITP state for {}", identcheckUuid);

        if (ITP_MOCK_UUIDS.containsKey(identcheckUuid)) {
            identCheckStatusMock(signer, identcheckUuid);
        } else {
            Mono<ItpCheckResponseBean> itpCheckResponseBeanMono = itpClient.identCheckStatus(identcheckUuid);
            itpCheckResponseBeanMono.subscribe(responseBean -> handleItpStatusResponse(responseBean, signer));
        }
    }

    private void identCheckStatusMock(DigitalContractSigner signer, String identcheckUuid) {
        log.info("Mock state {}", identcheckUuid);
        List<String> possibleMockUuids = ITP_MOCK_UUIDS.get(identcheckUuid);
        int randomElementIndex
                = ThreadLocalRandom.current().nextInt(possibleMockUuids.size()) % possibleMockUuids.size();
        identcheckUuid = possibleMockUuids.get(randomElementIndex);
        Mono<ItpCheckResponseBean> itpCheckResponseBeanMono = itpClient.mockedIdentCheckStatus(identcheckUuid);
        itpCheckResponseBeanMono.subscribe(responseBean -> handleItpStatusResponse(responseBean, signer));
    }

    private void handleItpStatusResponse(ItpCheckResponseBean itpCheckResponseBean, DigitalContractSigner signer) {
        String identcheckUuid = itpCheckResponseBean.getIdentcheckUuid();
        DigitalContractItpState itpState =
                DigitalContractItpState.findByInternalState(itpCheckResponseBean.getProcessStatus());
        log.info("New ITP state {}", itpState);
        ItpMaskedRequestBean maskedRequestBean = ItpMaskedRequestBean.builder()
                .identcheckUuid(identcheckUuid)
                .build();
        DigitalContractItpHistory itpHistory = new DigitalContractItpHistory();
        itpHistory.setItpRequest(maskedRequestBean);
        itpHistory.setItpResponse(itpCheckResponseBean);
        itpHistory.setSigner(signer);
        itpHistory.setState(itpState);
        itpHistoryRepository.save(itpHistory);

        signer.getCurrentState().setItpState(itpState);
        signer.getAesVerificationData().setIdentcheckUuid(identcheckUuid);
        contractSignerRepository.save(signer);
    }

}
