package de.immomio.service.property;

import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.LimitedAppointmentAcceptance;
import de.immomio.model.repository.shared.appointment.AppointmentRepository;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyService {

    private static final String ACCESS_NOT_ALLOWED_L = "ACCESS_NOT_ALLOWED_L";

    private static final String PROPERTY_NOT_FOUND = "PROPERTY_NOT_FOUND_L";

    private PropertyRepository propertyRepository;

    private UserSecurityService userSecurityService;

    private AppointmentRepository appointmentRepository;

    private AppointmentAcceptanceRepository appointmentAcceptanceRepository;

    @Autowired
    public PropertyService(
            PropertyRepository propertyRepository,
            UserSecurityService userSecurityService,
            AppointmentRepository appointmentRepository,
            AppointmentAcceptanceRepository appointmentAcceptanceRepository) {
        this.propertyRepository = propertyRepository;
        this.userSecurityService = userSecurityService;
        this.appointmentRepository = appointmentRepository;
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
    }

    public Property customFindById(Long id) {
        return propertyRepository.customFindOne(id);
    }

    public Property findById(Long id) throws NotAuthorizedException {
        Optional<Property> propertyOpt = propertyRepository.findById(id);

        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            if (userSecurityService.allowUserToReadProperty(property, userSecurityService.getPrincipalId())) {
                return property;
            } else {
                throw new NotAuthorizedException(ACCESS_NOT_ALLOWED_L);
            }
        } else {
            throw new NotAuthorizedException(PROPERTY_NOT_FOUND);
        }
    }

    public boolean appointmentSlotsAvailable(Property property) {
        List<Appointment> appointments = appointmentRepository.findByProperty(property);
        if (appointments.isEmpty()) {
            return false;
        }
        List<LimitedAppointmentAcceptance> appointmentAcceptances = appointmentAcceptanceRepository.findLimitedByAppointmentIn(appointments);
        int sumInviteCount = appointments.stream().mapToInt(Appointment::getMaxInviteeCount).sum();
        long activeAcceptances = appointmentAcceptances.stream()
                .filter(appointmentAcceptance -> appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE)
                .count();
        return sumInviteCount > activeAcceptances;
    }
}
