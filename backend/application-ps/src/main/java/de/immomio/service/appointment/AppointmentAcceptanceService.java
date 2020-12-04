package de.immomio.service.appointment;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class AppointmentAcceptanceService {

    public static final String ACCEPTANCE_ALREADY_CANCELED_L = "ACCEPTANCE_ALREADY_CANCELED_L";
    private static final String ACCEPTANCE_NOT_FOUND_L = "ACCEPTANCE_NOT_FOUND_L";
    private final AppointmentAcceptanceRepository appointmentAcceptanceRepository;

    private final AppointmentNotificationService notificationService;

    private final PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate;

    @Autowired
    public AppointmentAcceptanceService(
            AppointmentAcceptanceRepository appointmentAcceptanceRepository,
            AppointmentNotificationService notificationService,
            PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate
    ) {
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
        this.notificationService = notificationService;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
    }

    public AppointmentAcceptance cancel(AppointmentAcceptance appointmentAcceptance) {
        if (appointmentAcceptance == null) {
            throw new ApiValidationException(ACCEPTANCE_NOT_FOUND_L);
        }
        if(appointmentAcceptance.getState() == AppointmentAcceptanceState.CANCELED) {
            throw new ApiValidationException(ACCEPTANCE_ALREADY_CANCELED_L);
        }
        appointmentAcceptance.setState(AppointmentAcceptanceState.CANCELED);
        AppointmentAcceptance saved = appointmentAcceptanceRepository.save(appointmentAcceptance);
        notificationService.appointmentCanceled(saved);
        applicationIndexingDelegate.cancelledAppointment(saved.getApplication());
        return saved;
    }
}
