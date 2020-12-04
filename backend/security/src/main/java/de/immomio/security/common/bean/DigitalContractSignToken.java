package de.immomio.security.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Niklas Lindemann
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalContractSignToken extends AbstractToken {
    private static final long serialVersionUID = -73097122145787958L;
    private Long signerId;

}
