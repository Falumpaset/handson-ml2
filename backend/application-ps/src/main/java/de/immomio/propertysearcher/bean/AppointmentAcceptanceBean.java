package de.immomio.propertysearcher.bean;

import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class AppointmentAcceptanceBean  {

    private static final long serialVersionUID = 5310888004397067001L;

    private Long appointmentId;

    private Long id;

    private AppointmentAcceptanceState state;
}
