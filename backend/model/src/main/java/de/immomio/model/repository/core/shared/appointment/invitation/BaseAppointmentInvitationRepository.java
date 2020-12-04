package de.immomio.model.repository.core.shared.appointment.invitation;

import de.immomio.data.shared.entity.appointment.attendance.invitation.AppointmentInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseAppointmentInvitationRepository extends JpaRepository<AppointmentInvitation, Long> {
}

