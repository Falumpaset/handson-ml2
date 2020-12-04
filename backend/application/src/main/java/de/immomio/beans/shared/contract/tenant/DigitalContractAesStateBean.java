package de.immomio.beans.shared.contract.tenant;

import de.immomio.data.shared.entity.contract.signer.history.aes.itp.DigitalContractItpState;
import de.immomio.data.shared.entity.contract.signer.history.aes.schufa.DigitalContractSchufaState;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
public class DigitalContractAesStateBean implements Serializable {
    private static final long serialVersionUID = -9023473641078547482L;
    private DigitalContractSchufaState schufaState;
    private DigitalContractItpState itpState;
}
