package de.immomio.beans.shared.contract.onsite;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Data
public class DigitalContractOnsiteFlatVisitedBean implements Serializable {
    private static final long serialVersionUID = 4809189050543277614L;
    private Date flatVisited;
}
