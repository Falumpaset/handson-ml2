package de.immomio.model.repository.shared.appointment.acceptance;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.LimitedAppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.appointment.acceptance.BaseAppointmentAcceptanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "appointmentAcceptances")
public interface AppointmentAcceptanceRepository
        extends BaseAppointmentAcceptanceRepository, AppointmentAcceptanceRepositoryCustom {

    @Override
    @RestResource(exported = false)
    Optional<AppointmentAcceptance> findById(Long id);

    @Override
    @RestResource(exported = false)
    Page<AppointmentAcceptance> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void delete(AppointmentAcceptance appointment);

    @Override
    @RestResource(exported = false)
    AppointmentAcceptance save(AppointmentAcceptance appointmentAcceptance);

    @RestResource(exported = false)
    @Query("SELECT a from AppointmentAcceptance a where a.appointment = :appointment and a.state = :state and a.application.archived is null")
    Page<AppointmentAcceptance> findByAppointmentAndState(
            @Param("appointment") Appointment appointment,
            @Param("state") AppointmentAcceptanceState state,
            Pageable pageable);

    @PreAuthorize("#application.userProfile.user.id == principal?.id")
    Page<AppointmentAcceptance> findByApplication(
            @Param("application") PropertyApplication application,
            Pageable pageable);

    @PreAuthorize("#application.userProfile.user.id == principal?.id")
    Page<AppointmentAcceptance> findByApplicationAndState(
            @Param("application") PropertyApplication application,
            @Param("state") AppointmentAcceptanceState state,
            Pageable pageable);

    @Query(value = "SELECT o FROM #{#entityName} o INNER JOIN o.appointment ap INNER JOIN o.application apl " +
            "WHERE ap.id = :appointmentId AND apl.userProfile.user.id = ?#{principal.id} " +
            "ORDER BY ?#{#pageable}")
    Page<AppointmentAcceptance> findByAppointmentId(@Param("appointmentId") Long appointmentId, Pageable pageable);

    @RestResource(exported = false)
    @Query("SELECT COUNT(aa) " +
                   "FROM AppointmentAcceptance as aa " +
                   "WHERE aa.application.id = :applicationId AND aa.appointment.date < current_timestamp " +
                   "AND aa.state = 'ACTIVE'")
    Long getSizeOfAppointmentAcceptancesPast(
            @Param("applicationId") Long applicationId);

    @Override
    @RestResource(exported = false)
    List<AppointmentAcceptance> findByAppointmentAndStateAndEmailSent(
            Appointment appointment,
            AppointmentAcceptanceState state,
            boolean emailSent);

    @Modifying
    @Transactional
    @Query(value = "UPDATE shared.appointment_acceptance set state = 'CANCELED' WHERE application_id in :applicationIds",
            nativeQuery = true)
    void customCancelAll(@Param(value = "applicationIds") List<Long> applicationIds);


    @RestResource(exported = false)
    Optional<AppointmentAcceptance> findFirstByApplicationAndState(PropertyApplication application, AppointmentAcceptanceState state);

    @RestResource(exported = false)
    @Query("SELECT aa.state as state, aa.created as created, aa.appointment.date as appointmentDate, aa.appointment.id as appointmentId from AppointmentAcceptance aa where aa.application = :application")
    List<LimitedAppointmentAcceptance> findLimitedByApplication(@Param("application") PropertyApplication application);

    @RestResource(exported = false)
    @Query("SELECT aa.state as state, aa.created as created, aa.appointment.date as appointmentDate, aa.appointment.id as appointmentId from AppointmentAcceptance aa where aa.appointment in (:appointments)")
    List<LimitedAppointmentAcceptance> findLimitedByAppointmentIn(@Param("appointments") List<Appointment> appointments);
}
