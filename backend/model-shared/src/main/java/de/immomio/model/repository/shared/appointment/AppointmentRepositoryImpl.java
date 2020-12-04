package de.immomio.model.repository.shared.appointment;

import de.immomio.data.shared.entity.appointment.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * @author Maik Kingma
 */
public class AppointmentRepositoryImpl implements AppointmentRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public AppointmentRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(Appointment.class);
    }

    @Override
    public Appointment customFindOne(Long id) {
        return entityManager.find(Appointment.class, id);
    }

    @Override
    @Transactional
    public void customSave(Appointment appointment) {
        if (appointment.isNew()) {
            entityManager.persist(appointment);
        } else {
            entityManager.merge(appointment);
        }
    }
}
