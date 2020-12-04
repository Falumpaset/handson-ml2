package de.immomio.service.shared.contract;

import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistory;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractHistoryRepository;
import de.immomio.model.repository.core.shared.contract.BaseDigitalContractRepository;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractDigitalContractHistoryService<
        DCHR extends BaseDigitalContractHistoryRepository,
        DCR extends BaseDigitalContractRepository
        > {

    private DCHR historyRepository;

    private DCR contractRepository;

    public AbstractDigitalContractHistoryService(DCHR historyRepository, DCR contractRepository) {
        this.historyRepository = historyRepository;
        this.contractRepository = contractRepository;
    }

    public void historyInternalCreated(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.INTERNAL_CREATED);
    }

    public void historyExternalCreated(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.EXTERNAL_CREATED);
    }

    public void historyDocuSignCreated(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_CREATED);
    }

    public void historyDocuSignCreateFailed(DigitalContract digitalContract, DocuSignApiException e) {
        String docuSignResponse = e.getDocuSignResponseBody();
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_CREATE_FAILED, docuSignResponse);
    }

    public void historyInternalUpdated(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.INTERNAL_UPDATED);
    }

    public void historyDocuSignUpdated(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_UPDATED);
    }

    public void historyDocuSignUpdateFailed(DigitalContract digitalContract, DocuSignApiException e) {
        String docuSignResponse = e.getDocuSignResponseBody();
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_UPDATE_FAILED, docuSignResponse);
    }

    public void historyDocuSignSent(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_SENT);
    }

    public void historyDocuSignCompleted(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_COMPLETED);
    }

    public void historyInternalCanceled(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.INTERNAL_CANCELED);
    }

    public void historyInternalInterrupted(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.INTERNAL_INTERRUPTED);
    }

    public void historyDocusignUpdatedAfterInterrupted(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_UPDATED_AFTER_INTERRUPTED);
    }

    public void historyDocusignSentAfterInterrupted(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.DOCUSIGN_SENT_AFTER_INTERRUPTED);
    }

    public void historyContractDeleted(DigitalContract digitalContract) {
        createHistoryEntry(digitalContract, DigitalContractHistoryState.CONTRACT_DELETED);
    }

    private void createHistoryEntry(DigitalContract digitalContract, DigitalContractHistoryState state) {
        createHistoryEntry(digitalContract, state, null);
    }


    public boolean contractHistoryContainsState(DigitalContract digitalContract, DigitalContractHistoryState state) {
        return historyRepository.existsDigitalContractHistoryByDigitalContractAndState(digitalContract, state);
    }

    private void createHistoryEntry(
            DigitalContract digitalContract,
            DigitalContractHistoryState state,
            String docuSignResponse) {
        DigitalContractHistory history = new DigitalContractHistory();
        history.setDigitalContract(digitalContract);
        history.setState(state);
        history.setDocuSignResponse(docuSignResponse);
        historyRepository.save(history);
        digitalContract.setCurrentState(history.getState());
        contractRepository.save(digitalContract);
    }
}
