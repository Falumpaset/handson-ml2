package de.immomio.service.application;

import de.immomio.data.base.type.application.ApplicantStatus;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.LimitedAppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.appointment.AppointmentRepository;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyApplicationApplicantStatusService {
    private AppointmentRepository appointmentRepository;
    private AppointmentAcceptanceRepository appointmentAcceptanceRepository;

    @Autowired
    public PropertyApplicationApplicantStatusService(AppointmentRepository appointmentRepository,
            AppointmentAcceptanceRepository appointmentAcceptanceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
    }

    public ApplicantStatus calculateApplicantStatus(PropertyApplication application) {
        if (application.getProperty().getTenant() != null &&
                Objects.equals(application.getProperty().getTenant().getUserProfile(), application.getUserProfile())) {
            return ApplicantStatus.TENANT;
        }

        if (application.getStatus() == ApplicationStatus.INTENT) {
            return ApplicantStatus.INTENT;
        }
        if (application.getStatus() == ApplicationStatus.NO_INTENT) {
            return ApplicantStatus.NO_INTENT;
        }
        if (application.getStatus() == ApplicationStatus.REJECTED) {
            return ApplicantStatus.REJECTED;
        }

        if (application.getStatus() == ApplicationStatus.ACCEPTED) {
            return calculateApplicationAcceptedStatus(application);
        }
        return ApplicantStatus.WAITING_FOR_LANDLORD;

    }

    private ApplicantStatus calculateApplicationAcceptedStatus(PropertyApplication application) {

        List<LimitedAppointmentAcceptance> acceptances = appointmentAcceptanceRepository.findLimitedByApplication(application);
        boolean acceptedAppointment = acceptances.stream()
                .anyMatch(limitedAppointmentAcceptance -> limitedAppointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE);

        boolean attendedAppointment = acceptances.stream().anyMatch(appointmentAcceptance -> acceptanceIsActive(appointmentAcceptance)
                && appointmentIsInPast(appointmentAcceptance));

        if (acceptedAppointment) {
            if (attendedAppointment || application.isAskedForIntent()) {
                return ApplicantStatus.DECLARE_INTENT;
            }
            return ApplicantStatus.ATTENDING_TO_VIEWING;
        }
        if (appointmentsFullyBooked(application.getProperty())) {
            return ApplicantStatus.NO_OPEN_SLOTS;
        }

        return ApplicantStatus.INVITED_TO_VIEWING;
    }

    private boolean appointmentIsInPast(LimitedAppointmentAcceptance appointmentAcceptance) {
        return appointmentAcceptance.getAppointmentDate().before(new Date());
    }

    private boolean acceptanceIsActive(LimitedAppointmentAcceptance appointmentAcceptance) {
        return appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE;
    }

    private boolean appointmentsFullyBooked(Property property) {
        List<Appointment> appointments = appointmentRepository.findByProperty(property);
        return appointments.stream()
                .noneMatch(appointment -> appointment.dateIsAfterNow() &&
                        !appointment.isFull() &&
                        !appointment.isExclusive() &&
                        appointment.getState() == AppointmentState.ACTIVE);
    }
}
