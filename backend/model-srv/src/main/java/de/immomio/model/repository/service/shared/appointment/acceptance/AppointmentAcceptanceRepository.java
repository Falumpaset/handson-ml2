package de.immomio.model.repository.service.shared.appointment.acceptance;

import de.immomio.model.repository.core.shared.appointment.acceptance.BaseAppointmentAcceptanceRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "appointmentAcceptances")
public interface AppointmentAcceptanceRepository extends BaseAppointmentAcceptanceRepository {
}
