package de.immomio.model.repository.shared.appointment;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.model.repository.core.shared.appointment.BaseAppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "appointments")
public interface AppointmentRepository extends BaseAppointmentRepository, AppointmentRepositoryCustom {

    @Override
    @RestResource(exported = false)
    Page<Appointment> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void delete(Appointment appointment);

    @Override
    @PreAuthorize("#appointment.property.customer.id == principal?.customer.id")
    Appointment save(@Param("appointment") Appointment appointment);

    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.property p " +
            "WHERE p = :property AND o.state = :state " +
            "  AND (o.date >= :from OR cast(:from AS date) IS NULL) AND (o.date <= :to OR cast(:to AS date) IS NULL) " +
            "ORDER BY ?#{#pageable}")
    @PreAuthorize("#property.customer == principal?.customer " +
            "|| @userSecurityService.maySeeAppointments(#property, principal?.id)")
    @RestResource(exported = false)
    Page<Appointment> findByPropertyAndStateAndDateBetween(
            @Param("property") Property property,
            @Param("state") AppointmentState state,
            @DateTimeFormat(pattern = "yyyy/MM/dd") @Param("from") Date from,
            @DateTimeFormat(pattern = "yyyy/MM/dd") @Param("to") Date to,
            Pageable pageable
    );

    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.property p " +
            "WHERE p.customer.id = ?#{principal.customer.id} AND o.state = :state " +
            "  AND (o.date >= :from OR cast(:from AS date) IS NULL) AND (o.date <= :to OR cast(:to AS date) IS NULL) " +
            "ORDER BY ?#{#pageable}")
    @RestResource(exported = false)
    Page<Appointment> findByStateAndDateBetween(
            @Param("state") AppointmentState state,
            @DateTimeFormat(pattern = "yyyy/MM/dd") @Param("from") Date from,
            @DateTimeFormat(pattern = "yyyy/MM/dd") @Param("to") Date to,
            Pageable pageable
    );

    @Query("SELECT o FROM #{#entityName} o WHERE o.property.user in (:users) " +
            "AND o.state = :state AND (o.date >= :from OR cast(:from AS date) IS NULL) AND (o.date <= :to OR cast(:to AS date) IS NULL)" +
            "ORDER BY ?#{#pageable}")
    @RestResource(exported = false)
    Page<Appointment> findByUsersAndStateAndDateBetween(
            @Param("users") List<LandlordUser> users,
            @Param("state") AppointmentState state,
            @DateTimeFormat(pattern = "yyyy/MM/dd") @Param("from") Date from,
            @DateTimeFormat(pattern = "yyyy/MM/dd") @Param("to") Date to,
            Pageable pageable);

    @PreAuthorize("#property.customer == principal?.customer " +
            "|| @userSecurityService.maySeeAppointments(#property, principal?.id)")
    List<Appointment> findByPropertyAndState(
            @Param("property") Property property,
            @Param("state") AppointmentState state
    );

    @RestResource(exported = false)
    List<Appointment> findByProperty(Property property);


    @Query("SELECT count(o) FROM #{#entityName} o INNER JOIN o.property p " +
            "WHERE p.customer.id = ?#{principal.customer.id} " +
            "AND o.state = :state " +
            "AND (o.date > :date)")
    @RestResource(exported = false)
    Long countByStateAndDateAfter(@Param("state") AppointmentState state, @Param("date") Date date);

    @Query("SELECT count(o) FROM #{#entityName} o INNER JOIN o.property p " +
            "WHERE p.customer.id = ?#{principal.customer.id} " +
            "AND o.state = :state " +
            "AND (o.date < :date)")
    @RestResource(exported = false)
    Long countByStateAndDateBefore(@Param("state") AppointmentState state, @Param("date") Date date);

    @Override
    @RestResource(exported = false)
    List<Appointment> findAllByDateBetweenAndState(Date from, Date to, AppointmentState state);

    @Query("SELECT count(o) FROM #{#entityName} o " +
            "WHERE o.property.customer.id = ?#{principal.customer.id} AND o.property.user.id in (:users)" +
            "AND o.state = :state AND (o.date > :date)")
    @RestResource(exported = false)
    Long countByUsersAndStateAndDateAfter(@Param("users") List<Long> users, @Param("date") Date date,
                                          @Param("state") AppointmentState state);

    @Query("SELECT count(o) FROM #{#entityName} o " +
            "WHERE o.property.customer.id = ?#{principal.customer.id} AND o.property.user.id in (:users)" +
            "AND o.state = :state AND (o.date < :date)")
    @RestResource(exported = false)
    Long countByUsersAndStateAndDateBefore(@Param("users") List<Long> users, @Param("date") Date date,
                                           @Param("state") AppointmentState state);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o where o.id = :id and o.property.customer = :customer")
    Optional<Appointment> findByIdAndCustomer(@Param("id") Long id, @Param("customer") LandlordCustomer customer);
}
