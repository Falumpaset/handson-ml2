package de.immomio.model.repository.shared.appointment.acceptance;

import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;

/**
 * @author Maik Kingma
 */

public interface AppointmentAcceptanceRepositoryCustom {

    void customSave(AppointmentAcceptance appointment);

    AppointmentAcceptance customFindOne(Long id);
}
