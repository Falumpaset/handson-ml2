package de.immomio.beans.shared;

import de.immomio.data.shared.entity.appointment.Appointment;
import lombok.Getter;

/**
 * @author Niklas Lindemann
 */

@Getter
public class AppointmentCountBean extends AbstractAppointmentCountBean {

    private Appointment appointment;

    public AppointmentCountBean(Appointment appointment, Long past, Long upcoming) {
        super(past, upcoming);
        this.appointment = appointment;
    }
}
