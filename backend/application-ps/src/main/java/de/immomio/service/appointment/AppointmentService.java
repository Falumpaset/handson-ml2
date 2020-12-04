package de.immomio.service.appointment;

import de.immomio.common.ical.ICalEventHandler;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.invitation.AppointmentInvitation;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.propertysearcher.bean.AppointmentAcceptanceBean;
import de.immomio.propertysearcher.bean.AppointmentBundleBean;
import de.immomio.propertysearcher.bean.AppointmentInvitationBean;
import de.immomio.propertysearcher.bean.PropertyBean;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
@Slf4j
public class AppointmentService {

    private static final String NOT_INVITED_TO_VIEWING_YET = "NOT_INVITED_TO_VIEWING_YET_L";
    private static final String ONE_APPOINTMENT_ALREADY_ACCEPTED = "AT_LEAST_ONE_APPOINTMENT_ALREADY_ACCEPTED_L";
    private static final String MAX_INVITATION_DB_ERROR = "MAX_INVITEE_EXEEDED_ERROR_L";
    private static final String PROPERTIES_DO_NOT_MATCH = "PROPERTIES_DO_NOT_MATCH_L";
    private static final String APPOINTMENT_IS_EXCLUSIVE_FOR_ANOTHER_APPLICATION_L = "APPOINTMENT_IS_EXCLUSIVE_FOR_ANOTHER_APPLICATION_L";

    @Value("${appointment.durationInMinutes}")
    private int durationInMinutes;

    private final AppointmentAcceptanceRepository appointmentAcceptanceRepository;

    private final AppointmentNotificationService applicationNotificationService;

    private final PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate;

    @Autowired
    public AppointmentService(
            AppointmentAcceptanceRepository appointmentAcceptanceRepository,
            AppointmentNotificationService applicationNotificationService,
            PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate
    ) {
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
        this.applicationNotificationService = applicationNotificationService;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
    }

    public List<AppointmentBundleBean> getAppointmentBundleBeans(PropertySearcherUserProfile userProfile) {
        List<PropertyApplication> applications = userProfile.getApplications();

        return applications.stream()
                .filter(application -> application.getStatus() != ApplicationStatus.UNANSWERED && application.getStatus() != ApplicationStatus.REJECTED)
                .map(this::collectBundleBean)
                .sorted()
                .collect(Collectors.toList());
    }


    private AppointmentBundleBean collectBundleBean(PropertyApplication application) {
        AppointmentBundleBean bundleBean = new AppointmentBundleBean();
        Property property = application.getProperty();
        PropertyData data = property.getData();

        List<Appointment> futureAppointments =
                property.getAppointments()
                        .stream()
                        .filter(appointment -> shouldSeeAppointment(application, appointment))
                        .collect(Collectors.toList());

        List<AppointmentAcceptanceBean> appointmentAcceptanceBeans = futureAppointments.stream()
                .map(Appointment::getAppointmentAcceptances)
                .flatMap(Collection::stream)
                .filter(appointmentAcceptance -> appointmentAcceptance.getApplication().equals(application))
                .map(this::collectAppointmentAcceptanceBean)
                .collect(Collectors.toList());

        List<AppointmentInvitationBean> appointmentInvitationBeans = futureAppointments.stream()
                .map(Appointment::getAppointmentInvitations)
                .flatMap(Collection::stream)
                .filter(appointmentInvitation -> appointmentInvitation.getApplication().equals(application))
                .map(this::collectAppointmentInvitationBean)
                .collect(Collectors.toList());

        PropertyBean propertyBean = new PropertyBean();

        propertyBean.setAddress(data.getAddress());
        propertyBean.setName(data.getName());
        propertyBean.setPropertyId(property.getId());
        propertyBean.setImage(data.getTitleImage());
        propertyBean.setCustomerLogo(property.getCustomerLogo());
        propertyBean.setCustomerName(property.getCustomerName());

        bundleBean.getAppointmentAcceptances().addAll(appointmentAcceptanceBeans);
        bundleBean.getAppointmentInvitations().addAll(appointmentInvitationBeans);
        bundleBean.getAppointments().addAll(futureAppointments);
        bundleBean.setAppointments(sortAppointmentsByDate(bundleBean.getAppointments()));
        bundleBean.setProperty(propertyBean);
        bundleBean.setApplicationId(application.getId());


        return bundleBean;
    }

    private boolean shouldSeeAppointment(PropertyApplication application, Appointment appointment) {
        return appointment.getDate().compareTo(new Date()) >= 0
                && (!appointment.isFull() || isVisitingAppointment(application, appointment))
                && appointment.getState() == AppointmentState.ACTIVE
                && ((!appointment.isExclusive()) || appointment.getAppointmentInvitations().stream()
                .anyMatch(appointmentInvitation -> appointmentInvitation.getApplication() == application));
    }

    private boolean isVisitingAppointment(PropertyApplication application, Appointment appointment) {
        return appointment.getAppointmentAcceptances()
                .stream()
                .filter(appointmentAcceptance -> AppointmentAcceptanceState.ACTIVE == appointmentAcceptance.getState())
                .map(AppointmentAcceptance::getApplication)
                .anyMatch(acceptanceApplication -> acceptanceApplication.equals(application));
    }

    public File generateAppointmentIcsFile(Appointment appointment) throws IOException {
        ICalEventHandler iCalEventHandler = new ICalEventHandler();
        Property property = appointment.getProperty();

        return iCalEventHandler.createEvent(
                appointment.getId().toString(),
                ICalEventHandler.ICAL_TITLE,
                property.getData().getAddress().toString(),
                ICalEventHandler.LANGUAGE,
                appointment.getDate(),
                durationInMinutes
        );
    }

    public AppointmentAcceptance acceptViewing(Appointment appointment, PropertyApplication application) {
        if (userHasAppointmentAcceptanceForProperty(appointment.getProperty(), application)) {
            throw new ApiValidationException(ONE_APPOINTMENT_ALREADY_ACCEPTED);
        }

        if (!appointment.getProperty().getId().equals(application.getProperty().getId())) {
            throw new ApiValidationException(PROPERTIES_DO_NOT_MATCH);
        }

        if (ApplicationStatus.UNANSWERED == application.getStatus()) {
            throw new ApiValidationException(NOT_INVITED_TO_VIEWING_YET);
        }

        if (appointment.isFull()) {
            throw new ApiValidationException(MAX_INVITATION_DB_ERROR);
        }


        if (appointment.isExclusive() && appointment.getAppointmentInvitations()
                .stream()
                .noneMatch(appointmentInvitation -> appointmentInvitation.getApplication().equals(application))) {
            throw new ApiValidationException(APPOINTMENT_IS_EXCLUSIVE_FOR_ANOTHER_APPLICATION_L);
        }
        AppointmentAcceptance appointmentAcceptance = new AppointmentAcceptance();
        appointmentAcceptance.setAppointment(appointment);
        appointmentAcceptance.setApplication(application);
        appointmentAcceptance.setState(AppointmentAcceptanceState.ACTIVE);

        AppointmentAcceptance saved = appointmentAcceptanceRepository.save(appointmentAcceptance);

        applicationNotificationService.appointmentAccepted(saved);
        applicationIndexingDelegate.acceptedAppointment(application);

        return saved;
    }

    public AppointmentAcceptanceBean collectAppointmentAcceptanceBean(AppointmentAcceptance appointmentAcceptance) {
        AppointmentAcceptanceBean bean = new AppointmentAcceptanceBean();
        bean.setAppointmentId(appointmentAcceptance.getAppointment().getId());
        bean.setState(appointmentAcceptance.getState());
        bean.setId(appointmentAcceptance.getId());

        return bean;
    }

    private AppointmentInvitationBean collectAppointmentInvitationBean(AppointmentInvitation appointmentInvitation) {
        AppointmentInvitationBean bean = new AppointmentInvitationBean();
        bean.setAppointmentId(appointmentInvitation.getAppointment().getId());
        bean.setId(appointmentInvitation.getId());

        return bean;
    }

    private List<Appointment> sortAppointmentsByDate(List<Appointment> appointments) {
        return appointments.stream().sorted(Comparator.comparing(Appointment::getDate)).collect(Collectors.toList());
    }

    private boolean userHasAppointmentAcceptanceForProperty(Property property, PropertyApplication application) {
        AtomicBoolean returnVal = new AtomicBoolean(false);
        property.getAppointments().forEach(appointment -> {
            appointment.getAppointmentAcceptances().forEach(appointmentAcceptance -> {
                if (appointmentAcceptance.getApplication().equals(application)
                        && appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE
                        && appointment.getDate().after(new Date())) {
                    returnVal.set(true);
                }
            });
        });
        return returnVal.get();
    }
}
