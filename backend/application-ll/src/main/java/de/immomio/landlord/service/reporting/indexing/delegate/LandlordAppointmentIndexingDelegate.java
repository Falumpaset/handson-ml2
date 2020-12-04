package de.immomio.landlord.service.reporting.indexing.delegate;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.landlord.service.reporting.indexing.LandlordAppointmentIndexingSenderService;
import de.immomio.landlord.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class LandlordAppointmentIndexingDelegate extends BaseLandlordIndexingDelegate {

    private final LandlordAppointmentIndexingSenderService landlordAppointmentIndexingService;

    @Autowired
    public LandlordAppointmentIndexingDelegate(
            LandlordAppointmentIndexingSenderService landlordAppointmentIndexingService,
            UserSecurityService userSecurityService
    ) {
        super(userSecurityService);
        this.landlordAppointmentIndexingService = landlordAppointmentIndexingService;
    }

    public void appointmentCreated(Appointment appointment) {
        landlordAppointmentIndexingService.appointmentCreated(appointment, getPrincipal());
    }

    public void appointmentUpdated(Appointment appointment) {
        landlordAppointmentIndexingService.appointmentUpdated(appointment, getPrincipal());
    }

    public void appointmentCanceled(Appointment appointment) {
        landlordAppointmentIndexingService.appointmentCanceled(appointment, getPrincipal());
    }

}
