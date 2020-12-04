package de.immomio.beans.shared.contract.tenant.token;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Andreas Hansen
 */

@Getter
@Setter
public class DigitalContractFlatVisitedTokenBean extends DigitalContractSigningTokenBean {
    private static final long serialVersionUID = -5841446666618265888L;

    private Date flatVisited;
}
