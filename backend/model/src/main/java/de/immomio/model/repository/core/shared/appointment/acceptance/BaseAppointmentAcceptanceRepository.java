package de.immomio.model.repository.core.shared.appointment.acceptance;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "appointmentAcceptances")
public interface BaseAppointmentAcceptanceRepository extends JpaRepository<AppointmentAcceptance, Long> {
    List<AppointmentAcceptance> findByAppointmentAndStateAndEmailSent(Appointment appointment, AppointmentAcceptanceState state, boolean emailSent);
}

