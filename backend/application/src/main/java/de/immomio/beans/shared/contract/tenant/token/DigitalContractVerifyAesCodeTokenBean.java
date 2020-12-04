package de.immomio.beans.shared.contract.tenant.token;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Andreas Hansen
 */

@Getter
@Setter
public class DigitalContractVerifyAesCodeTokenBean extends DigitalContractSigningTokenBean {
    private static final long serialVersionUID = 8682842997346505843L;

    private String aesCode;
}
