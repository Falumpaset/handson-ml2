package de.immomio.beans.shared.contract.tenant.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalContractSigningTokenBean implements Serializable {
    private static final long serialVersionUID = -8175417982456406549L;

    private String token;
    private String redirectUrl;
}
