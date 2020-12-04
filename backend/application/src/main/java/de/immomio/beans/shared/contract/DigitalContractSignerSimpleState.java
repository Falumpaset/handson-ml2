package de.immomio.beans.shared.contract;

import de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryStateConstants.CANCELED_STATES;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryStateConstants.IN_PROGRESS_STATES;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryStateConstants.SIGNED_STATES;
import static de.immomio.data.shared.entity.contract.signer.history.DigitalContractSignerHistoryStateConstants.WAITING_STATES;

/**
 * @author Niklas Lindemann
 */

@AllArgsConstructor
@Schema(name = "SignerStatus", description = "the possible states of contract signers")
public enum DigitalContractSignerSimpleState {
    WAITING(Stream.of(WAITING_STATES, IN_PROGRESS_STATES).flatMap(Collection::stream).collect(Collectors.toList())),
    SIGNED(SIGNED_STATES),
    CANCELED(CANCELED_STATES);

    @Getter
    private List<DigitalContractSignerHistoryState> internalStates;

    public static DigitalContractSignerSimpleState getByContractState(DigitalContractSignerHistoryState historyState) {
        return Arrays.stream(DigitalContractSignerSimpleState.values())
                .filter(digitalContractFilterState -> digitalContractFilterState.getInternalStates()
                        .contains(historyState)).findFirst()
                .orElse(null);
    }
}
