package de.immomio.model.contract;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Niklas Lindemann
 */
@Schema(name = "ContractStatus", description = "the possible states of a contract")
public enum ApiContractStatus {
    IN_PROGRESS, FINISHED, CANCELED
}
