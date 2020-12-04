package de.immomio.landlord.service.appointment.acceptance;

import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.utils.repository.ResourcePageableUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hansen
 */

@Service
public class AppointmentAcceptanceService {

    private final AppointmentAcceptanceRepository appointmentAcceptanceRepository;

    @Autowired
    public AppointmentAcceptanceService(
            AppointmentAcceptanceRepository appointmentAcceptanceRepository
    ) {
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
    }

    public Page<AppointmentAcceptance> findByAppointmentAndState(
            Appointment appointment,
            AppointmentAcceptanceState state,
            int page,
            int size,
            String sortStr
    ) {
        Pageable pageable = ResourcePageableUtils.pageableOf(page, size, sortStr);

        return appointmentAcceptanceRepository.findByAppointmentAndState(appointment, state, pageable);
    }

    public void cancelAllAttendances(List<PropertyApplication> applications) {
        List<Long> applicationIds = applications.stream().map(AbstractEntity::getId).collect(Collectors.toList());
        appointmentAcceptanceRepository.customCancelAll(applicationIds);
    }

}
