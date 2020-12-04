package de.immomio.model.repository.core.shared.appointment;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "appointments")
public interface BaseAppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT DISTINCT o FROM #{#entityName} o JOIN fetch o.appointmentAcceptances aa " +
                   "WHERE o.date >= :from AND o.date <= :to AND o.state = :state")
    @RestResource(exported = false)
    List<Appointment> findAllByDateBetweenAndState(
            @Param("from") Date from,
            @Param("to") Date to,
            @Param("state") AppointmentState state
    );
}
