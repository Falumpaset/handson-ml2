package de.immomio.reporting.model.event.customer;

import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.reporting.model.beans.ReportingPropertyApplicationBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class ApplicationEvent extends AbstractCustomerEvent implements Serializable {
    private static final long serialVersionUID = 9190864488080869683L;

    private ReportingPropertyApplicationBean application;

    private ApplicationEventType eventType;

}
