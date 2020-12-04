package de.immomio.landlord.listener.appointment;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.landlord.service.appointment.AppointmentNotificationService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordAppointmentIndexingDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler()
public class AppointmentListener {

    private final AppointmentNotificationService appointmentNotificationService;

    private final LandlordAppointmentIndexingDelegate appointmentIndexingDelegate;

    @Autowired
    public AppointmentListener(
            AppointmentNotificationService appointmentNotificationService,
            LandlordAppointmentIndexingDelegate appointmentIndexingDelegate
    ) {
        this.appointmentNotificationService = appointmentNotificationService;
        this.appointmentIndexingDelegate = appointmentIndexingDelegate;
    }

    @HandleAfterCreate
    public void notifyAfterAppointmentCreated(Appointment appointment) {
        appointmentNotificationService.publicAppointmentCreated(appointment);
        appointmentIndexingDelegate.appointmentCreated(appointment);
    }
}
