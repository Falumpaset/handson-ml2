package de.immomio.beans.shared.contract;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Andreas Hansen
 */

@Data
@Builder
public class DigitalContractSignatureMappingBean implements Serializable {

    private static final long serialVersionUID = -4156198779106228160L;

    private UUID internalContractId;
    private String embeddedSendingRedirectUrl;

}
