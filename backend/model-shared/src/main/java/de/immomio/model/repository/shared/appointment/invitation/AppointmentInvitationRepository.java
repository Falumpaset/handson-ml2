package de.immomio.model.repository.shared.appointment.invitation;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.invitation.AppointmentInvitation;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.appointment.invitation.BaseAppointmentInvitationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentInvitationRepository extends BaseAppointmentInvitationRepository {
    List<AppointmentInvitation> findAllByAppointment(Appointment appointment);

    Long countByApplication(PropertyApplication application);

}
