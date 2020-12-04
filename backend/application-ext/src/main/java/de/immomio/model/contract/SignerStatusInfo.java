package de.immomio.model.contract;

import de.immomio.beans.shared.contract.DigitalContractSignerSimpleState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */
@Getter
@Setter
public class SignerStatusInfo extends SignerInfo {
    private static final long serialVersionUID = -8093468231558627787L;

    private DigitalContractSignerSimpleState status;
}
