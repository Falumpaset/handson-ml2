package de.immomio.service.shared.contract;

import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistory;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import de.immomio.docusign.service.beans.DocuSignRecipientStatusType;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerHistoryRepository;
import de.immomio.model.repository.core.shared.contract.signer.BaseDigitalContractSignerRepository;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractDigitalContractSignerHistoryService<
        DCSHR extends BaseDigitalContractSignerHistoryRepository,
        DCSR extends BaseDigitalContractSignerRepository
        > {

    private final DCSHR signerHistoryRepository;

    private final DCSR signerRepository;

    public AbstractDigitalContractSignerHistoryService(DCSHR signerHistoryRepository, DCSR signerRepository) {
        this.signerHistoryRepository = signerHistoryRepository;
        this.signerRepository = signerRepository;
    }

    public void updateSignerHistory(DigitalContractSignerHistoryState state, DigitalContractSigner signer) {
        DigitalContractSignerHistoryState currentSignerState = signer.getCurrentState().getSignerState();
        if (currentSignerState == null || (state.getLevel() > currentSignerState.getLevel())) {
            DigitalContractSignerHistory history = new DigitalContractSignerHistory();
            history.setSigner(signer);
            history.setState(state);
            signerHistoryRepository.save(history);

            signer.getCurrentState().setSignerState(history.getState());
            signerRepository.save(signer);
        }
    }

    public void updateDocusignStatus(DigitalContractSigner signer, DocuSignRecipientStatusType docuSignStatus) {
        switch (docuSignStatus) {
            case SIGNER_STATUS_CREATED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_CREATED, signer);
                break;
            case SIGNER_STATUS_SENT:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_SENT, signer);
                break;
            case SIGNER_STATUS_SIGNED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_SIGNED, signer);
                break;
            case SIGNER_STATUS_DECLINED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_DECLINED, signer);
                break;
            case SIGNER_STATUS_FAXPENDING:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_FAXPENDING, signer);
                break;
            case SIGNER_STATUS_AUTORESPONDED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_AUTORESPONDED, signer);
                break;
            case SIGNER_STATUS_DELIVERED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_DELIVERED, signer);
                break;
            case SIGNER_STATUS_VOIDED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_VOIDED, signer);
                break;
            case SIGNER_STATUS_COMPLETED:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_COMPLETED, signer);
                break;
            default:
                updateSignerHistory(DigitalContractSignerHistoryState.DOCUSIGN_CREATED, signer);
        }
    }

    public void signerHistoryMailSent(DigitalContractSigner signer) {
        updateSignerHistory(DigitalContractSignerHistoryState.INTERNAL_MAIL_SENT, signer);
    }

    public void signerHistoryItpMailSent(DigitalContractSigner signer) {
        updateSignerHistory(DigitalContractSignerHistoryState.INTERNAL_ITP_MAIL_SENT, signer);
    }


}
