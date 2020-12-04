package de.immomio.beans.shared.contract;

import de.immomio.data.shared.entity.contract.history.DigitalContractHistoryState;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static de.immomio.data.shared.entity.contract.history.DigitalContractHistoryStateConstants.CANCELED_STATES;
import static de.immomio.data.shared.entity.contract.history.DigitalContractHistoryStateConstants.CONFIGURATION_NEEDED_STATES;
import static de.immomio.data.shared.entity.contract.history.DigitalContractHistoryStateConstants.FINISHED_STATES;
import static de.immomio.data.shared.entity.contract.history.DigitalContractHistoryStateConstants.IN_PROGRESS_STATES;
import static de.immomio.data.shared.entity.contract.history.DigitalContractHistoryStateConstants.PROTECTED_STATES;

/**
 * @author Niklas Lindemann
 */

@Getter
public enum DigitalContractSimpleState {

    CONFIGURATION_NEEDED(CONFIGURATION_NEEDED_STATES),
    IN_PROGRESS(IN_PROGRESS_STATES),
    PROTECTED(PROTECTED_STATES),
    FINISHED(FINISHED_STATES),
    CANCELED(CANCELED_STATES);

    private final List<DigitalContractHistoryState> internalStates;

    DigitalContractSimpleState(List<DigitalContractHistoryState> internalStates) {
        this.internalStates = internalStates;
    }

    public static DigitalContractSimpleState getByContractState(DigitalContractHistoryState historyState) {
        return Arrays.stream(DigitalContractSimpleState.values())
                .filter(digitalContractFilterState -> digitalContractFilterState.getInternalStates()
                        .contains(historyState)).findFirst()
                .orElse(null);
    }
}
