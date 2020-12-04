package de.immomio.service.application;

import de.immomio.data.shared.bean.application.PropertySearcherGuestPropertyApplicationBean;
import de.immomio.data.shared.bean.application.PropertySearcherPropertyApplicationBean;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.LimitedAppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyApplicationCollectionService {

    private final AppointmentAcceptanceRepository appointmentAcceptanceRepository;
    private final PropertyApplicationApplicantStatusService applicantStatusService;
    private final PropertyApplicationConverter applicationConverter;
    private final PropertyService propertyService;

    @Autowired
    public PropertyApplicationCollectionService(AppointmentAcceptanceRepository appointmentAcceptanceRepository,
            PropertyApplicationApplicantStatusService applicantStatusService,
            PropertyApplicationConverter applicationConverter, PropertyService propertyService) {
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
        this.applicantStatusService = applicantStatusService;
        this.applicationConverter = applicationConverter;
        this.propertyService = propertyService;
    }

    public PropertySearcherPropertyApplicationBean collectApplicationBean(PropertyApplication application) {
        List<LimitedAppointmentAcceptance> acceptances = appointmentAcceptanceRepository.findLimitedByApplication(application);
        return applicationConverter.convertToPropertySearcherPropertyApplicationBean(application,
                applicantStatusService.calculateApplicantStatus(application), propertyService.appointmentSlotsAvailable(application.getProperty()),
                acceptances);
    }

    public PropertySearcherGuestPropertyApplicationBean collectGuestApplicationBean(PropertyApplication application) {
        List<LimitedAppointmentAcceptance> acceptances = appointmentAcceptanceRepository.findLimitedByApplication(application);
        return applicationConverter.convertToPropertySearcherGuestPropertyApplicationBean(application,
                applicantStatusService.calculateApplicantStatus(application), propertyService.appointmentSlotsAvailable(application.getProperty()),
                acceptances);
    }
}
