package de.immomio.beans.shared.contract.tenant.token;

import lombok.Data;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Data
public class DigitalContractStartAesTokenBean extends DigitalContractSigningTokenBean {
    private static final long serialVersionUID = 5506886273855836379L;

    private String iban;

    private Date dateOfBirth;
}
