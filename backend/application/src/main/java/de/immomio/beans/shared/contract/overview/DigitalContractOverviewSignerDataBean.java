package de.immomio.beans.shared.contract.overview;

import de.immomio.beans.shared.contract.DigitalContractSignerSimpleState;
import de.immomio.data.shared.entity.contract.signer.SignerCurrentStateBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Andreas Hansen
 */

@Data
@Builder
@AllArgsConstructor
public class DigitalContractOverviewSignerDataBean implements Serializable {

    private static final long serialVersionUID = 4796488030707183349L;

    private String firstname;

    private String lastname;

    private DigitalContractSignerSimpleState status;

    private String email;

    private UUID internalSignerId;

    private SignerCurrentStateBean currentState;

    private boolean onsiteHost;
}
