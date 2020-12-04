package de.immomio.model.repository.service.shared.appointment;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.model.repository.core.shared.appointment.BaseAppointmentRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "appointments")
public interface AppointmentRepository extends BaseAppointmentRepository {
    List<Appointment> findByProperty(@Param("property") Property property);

    @RestResource(exported = false)
    @Query("SELECT a from Appointment  a where a.property.customer = :customer and a.agentInfo IS NULL")
    List<Appointment> findByCustomer(LandlordCustomer customer);
}
