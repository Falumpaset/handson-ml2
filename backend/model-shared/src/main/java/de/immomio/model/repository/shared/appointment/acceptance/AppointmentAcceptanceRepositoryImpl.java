package de.immomio.model.repository.shared.appointment.acceptance;

import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * @author Maik Kingma
 */

public class AppointmentAcceptanceRepositoryImpl implements AppointmentAcceptanceRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public AppointmentAcceptanceRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(AppointmentAcceptance.class);
    }

    @Override
    public AppointmentAcceptance customFindOne(Long id) {
        return entityManager.find(AppointmentAcceptance.class, id);
    }

    @Override
    @Transactional
    public void customSave(AppointmentAcceptance appointmentAcceptance) {
        if (appointmentAcceptance.isNew()) {
            appointmentAcceptance.setCreated(new Date());
            entityManager.persist(appointmentAcceptance);
        } else {
            appointmentAcceptance.setUpdated(new Date());
            entityManager.merge(appointmentAcceptance);
        }
    }
}
