package de.immomio.beans.shared.contract.tenant.token;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Niklas Lindemann
 */

@Data
public class DigitalContractConfirmSchufaTokenBean extends DigitalContractSigningTokenBean {
    private static final long serialVersionUID = 6109530770008560274L;

    @NotNull
    private boolean confirmed;
}
