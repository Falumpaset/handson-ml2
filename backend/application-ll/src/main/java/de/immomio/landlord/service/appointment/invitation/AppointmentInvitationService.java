package de.immomio.landlord.service.appointment.invitation;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.invitation.AppointmentInvitation;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.application.LandlordPropertyApplicationService;
import de.immomio.landlord.service.appointment.AppointmentNotificationService;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.appointment.invitation.AppointmentInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class AppointmentInvitationService {
    public static final String APPLICATION_MUST_BE_IN_CORRECT_STATE_L = "APPLICATION_MUST_BE_IN_CORRECT_STATE_L";
    public static final String PROPERTIES_DO_NOT_MATCH_L = "PROPERTIES_DO_NOT_MATCH_L";
    public static final String USER_IS_ANONYMOUS_L = "USER_IS_ANONYMOUS_L";
    private final AppointmentInvitationRepository appointmentInvitationRepository;

    private final PropertyApplicationRepository applicationRepository;

    private final LandlordPropertyApplicationService applicationService;

    private final AppointmentNotificationService notificationService;

    @Autowired
    public AppointmentInvitationService(
            AppointmentInvitationRepository appointmentInvitationRepository,
            PropertyApplicationRepository applicationRepository,
            LandlordPropertyApplicationService applicationService, AppointmentNotificationService notificationService
    ) {
        this.appointmentInvitationRepository = appointmentInvitationRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
        this.notificationService = notificationService;
    }

    public AppointmentInvitation create(Appointment appointment, Long applicationId) {

        PropertyApplication application = applicationService.findById(applicationId).orElseThrow();
        if (application.getStatus() == ApplicationStatus.REJECTED || application.getStatus() == ApplicationStatus.NO_INTENT) {
            throw new ApiValidationException(APPLICATION_MUST_BE_IN_CORRECT_STATE_L);
        }
        if (!appointment.getProperty().equals(application.getProperty())) {
            throw new ApiValidationException(PROPERTIES_DO_NOT_MATCH_L);
        }
        if (application.getUserProfile().isAnonymous()) {
            throw new ApiValidationException(USER_IS_ANONYMOUS_L);
        }
        if (application.getStatus() == ApplicationStatus.UNANSWERED) {
            applicationService.updateStatus(application, ApplicationStatus.ACCEPTED);
        }
        AppointmentInvitation appointmentInvitation = new AppointmentInvitation();
        appointmentInvitation.setAppointment(appointment);
        appointmentInvitation.setApplication(application);

        return appointmentInvitationRepository.save(appointmentInvitation);
    }

}
