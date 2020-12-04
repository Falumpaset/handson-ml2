package de.immomio.model.repository.shared.appointment;

import de.immomio.data.shared.entity.appointment.Appointment;

/**
 * @author Maik Kingma
 */

public interface AppointmentRepositoryCustom {

    void customSave(Appointment appointment);

    Appointment customFindOne(Long id);
}
