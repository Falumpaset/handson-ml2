package de.immomio.landlord.service.reporting.indexing;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.enums.AppointmentEventType;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.AppointmentEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordAppointmentIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    @Autowired
    public LandlordAppointmentIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    @Async
    public void appointmentCreated(Appointment appointment, LandlordUser user) {
        indexAppointment(appointment, AppointmentEventType.APPOINTMENT_CREATED, user);
    }

    @Async
    public void appointmentUpdated(Appointment appointment, LandlordUser user) {
        indexAppointment(appointment, AppointmentEventType.APPOINTMENT_UPDATED, user);
    }

    @Async
    public void appointmentCanceled(Appointment appointment, LandlordUser user) {
        indexAppointment(appointment, AppointmentEventType.APPOINTMENT_CANCELED, user);
    }

    private void indexAppointment(Appointment appointment, AppointmentEventType eventType, LandlordUser user) {
        AppointmentEvent event = createAppointmentEvent(appointment, new ReportingEditorBean(user), eventType);
        processAppointmentIndexing(user.getCustomer(), event);
    }

}
