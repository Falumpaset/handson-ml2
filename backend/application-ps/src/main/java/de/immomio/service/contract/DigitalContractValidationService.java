package de.immomio.service.contract;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Service
public class DigitalContractValidationService {

    public static final String NOT_ALLOWED_TO_UPDATE_DATA_NOT_CORRECT_STATE_L = "NOT_ALLOWED_TO_UPDATE_SIGNER_NOT_CORRECT_STATE_L";
    public static final String NOT_ALLOWED_TO_MODIFY_CONTRACT_L = "NOT_ALLOWED_TO_MODIFY_CONTRACT_L";
    public static final String SIGNER_MUST_BE_TENANT_L = "SIGNER_MUST_BE_TENANT_L";
    private final List<DigitalContractHistoryState> FINISHED_STATES = List.of(DigitalContractHistoryState.DOCUSIGN_COMPLETED, DigitalContractHistoryState.INTERNAL_CANCELED);

    public void validateSignerTypeTenant(DigitalContractSigner signer) {
        if (signer.getType() != DigitalContractSignerType.TENANT) {
            throw new ApiValidationException(SIGNER_MUST_BE_TENANT_L);
        }
    }

    public void validateCurrentSignerState(DigitalContractSigner signer, DigitalContractSignerHistoryState state) {
        if (signer.getCurrentState().getSignerState().getLevel() >= state.getLevel()) {
            throw new ApiValidationException(NOT_ALLOWED_TO_UPDATE_DATA_NOT_CORRECT_STATE_L);
        }
    }

    public void validateContractFinished(DigitalContract contract) {
        if (FINISHED_STATES.contains(contract.getCurrentState())) {
            throw new ApiValidationException(NOT_ALLOWED_TO_MODIFY_CONTRACT_L);
        }
    }

}
