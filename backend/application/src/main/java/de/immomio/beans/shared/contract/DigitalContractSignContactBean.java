package de.immomio.beans.shared.contract;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
public class DigitalContractSignContactBean implements Serializable {
    private static final long serialVersionUID = -641842296746987626L;
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
}
