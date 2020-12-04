package de.immomio.reporting.model.event.customer;

import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.reporting.model.beans.ReportingPropertyBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */
@Getter
@Setter
public class PropertyEvent extends AbstractCustomerEvent implements Serializable {
    private static final long serialVersionUID = - 8250994235416298755L;

    private ReportingPropertyBean property;
    private PropertyEventType eventType;

}
