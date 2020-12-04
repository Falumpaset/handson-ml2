package de.immomio.beans.shared;

import de.immomio.data.shared.entity.appointment.Appointment;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * @author Niklas Lindemann
 */

@Getter
public class PageAppointmentCountBean extends AbstractAppointmentCountBean {
    private PagedModel<EntityModel<Appointment>> appointments;

    public PageAppointmentCountBean(PagedModel<EntityModel<Appointment>> appointments, Long past, Long upcoming) {
        super(past, upcoming);
        this.appointments = appointments;
    }
}
