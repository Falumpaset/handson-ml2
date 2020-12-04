package de.immomio.reporting.model.event.customer;

import de.immomio.data.landlord.entity.user.reporting.enums.AppointmentEventType;
import de.immomio.reporting.model.beans.ReportingAppointmentBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class AppointmentEvent extends AbstractCustomerEvent implements Serializable {
    private static final long serialVersionUID = 3190711528485562062L;

    private ReportingAppointmentBean appointment;
    private AppointmentEventType eventType;
}
