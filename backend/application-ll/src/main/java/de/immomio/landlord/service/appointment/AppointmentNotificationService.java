package de.immomio.landlord.service.appointment;

import de.immomio.common.ical.ICalEventHandler;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.invitation.AppointmentInvitation;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.model.repository.shared.appointment.invitation.AppointmentInvitationRepository;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.landlord.AbstractNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AppointmentNotificationService extends AbstractNotificationService {

    private static final String APPOINTMENT_CREATED_SUBJECT_KEY = "invitation.created.subject";

    private static final String APPOINTMENT_CANCELED_SUBJECT_KEY = "invitation.canceled.subject";
    private static final String APPOINTMENT_EXCLUSIVE_CANCELED_SUBJECT_KEY = "invitation.exclusive.canceled.subject";

    private static final String VIEWING_CHANGED_SUBJECT_KEY = "property.viewing.changed.subject";

    private static final String INVITE_TO_VIEWING_SUBJECT_KEY = "property.invite.to.viewing.subject";

    @Value("${appointment.durationInMinutes}")
    private int durationInMinutes;

    private EmailModelProvider emailModelProvider;
    private final AppointmentInvitationRepository appointmentInvitationRepository;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public AppointmentNotificationService(EmailModelProvider emailModelProvider,
            AppointmentInvitationRepository appointmentInvitationRepository,
            RabbitTemplate rabbitTemplate,
            JWTTokenService jwtTokenService) {
        super(rabbitTemplate);
        this.emailModelProvider = emailModelProvider;
        this.appointmentInvitationRepository = appointmentInvitationRepository;
        this.jwtTokenService = jwtTokenService;
    }

    public void publicAppointmentCreated(Appointment appointment) {
        appointment.getProperty().getPropertyApplications()
                .stream()
                .filter(this::isAccepted)
                .filter(this::notAttendedAppointment)
                .forEach(application -> {
                    sendNotification(MailTemplate.INVITATION_CREATED, APPOINTMENT_CREATED_SUBJECT_KEY, appointment, application.getUserProfile(), application);
                });
    }

    public void exclusiveAppointmentCreated(Appointment appointment) {
        List<AppointmentInvitation> invitations = appointmentInvitationRepository.findAllByAppointment(appointment);
        invitations.forEach(appointmentInvitation -> {
            sendNotification(MailTemplate.EXCLUSIVE_INVITATION_CREATED, APPOINTMENT_CREATED_SUBJECT_KEY, appointment, appointmentInvitation.getApplication().getUserProfile(), appointmentInvitation.getApplication());
        });
    }

    public void appointmentInvitationCanceled(AppointmentInvitation invitation) {
        sendNotification(MailTemplate.EXCLUSIVE_INVITATION_CANCELED_TO_PS, APPOINTMENT_EXCLUSIVE_CANCELED_SUBJECT_KEY, invitation.getAppointment(), invitation.getApplication().getUserProfile());
    }

    private boolean isAccepted(PropertyApplication propertyApplication) {
        return propertyApplication.getStatus() == ApplicationStatus.ACCEPTED;
    }

    private boolean notAttendedAppointment(PropertyApplication propertyApplication) {
        return propertyApplication.getAppointmentAcceptances()
                .stream()
                .noneMatch(appointmentAcceptance ->
                        appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE);
    }

    public void appointmentCanceled(List<AppointmentAcceptance> acceptances) {
        acceptances.forEach(appointmentAcceptance -> {
            PropertySearcherUserProfile userProfile = appointmentAcceptance.getApplication().getUserProfile();
            if (appointmentAcceptance.getAppointment().isExclusive()) {
                sendNotification(
                        MailTemplate.EXCLUSIVE_INVITATION_CANCELED_TO_PS,
                        APPOINTMENT_CANCELED_SUBJECT_KEY,
                        appointmentAcceptance.getAppointment(),
                        userProfile);
            } else {
                sendNotification(
                        MailTemplate.INVITATION_CANCELED,
                        APPOINTMENT_CANCELED_SUBJECT_KEY,
                        appointmentAcceptance.getAppointment(),
                        userProfile);
            }
        });
    }

    public void appointmentChanged(Appointment appointment) {
        try {
            Map<String, File> attachmentFiles = new HashMap<>();
            addInviteIcsToAttachments(attachmentFiles, appointment);

            appointment.getAppointmentAcceptances()
                    .stream()
                    .filter(this::isAppointmentAcceptancesActive)
                    .forEach(appointmentAcceptance -> {
                        sendAppointmentChangeNotification(appointment, appointmentAcceptance.getApplication().getUserProfile(), attachmentFiles);
                    });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean isAppointmentAcceptancesActive(AppointmentAcceptance appointmentAcceptance) {
        return appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE;
    }

    public void inviteToViewings(Appointment appointment, List<String> emails) {
        try {
            Map<String, File> attachmentFiles = new HashMap<>();
            addInviteIcsToAttachments(attachmentFiles, appointment);

            emails.forEach(email -> sendInviteToViewingNotification(appointment, email, attachmentFiles));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendNotification(MailTemplate template, String subject, Appointment appointment,
            PropertySearcherUserProfile userProfile) {
        sendNotification(template, subject, appointment, userProfile, null);
    }

    private void sendNotification(MailTemplate template, String subject, Appointment appointment,
            PropertySearcherUserProfile userProfile, PropertyApplication application) {
        Property property = appointment.getProperty();

        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(userProfile, property);
        emailModelProvider.appendFormattedDate(appointment.getDate(), model,
                ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);
        if (application != null) {
            emailModelProvider.appendApplicationId(model, application);
            if (application.getUserProfile().getType() == PropertySearcherUserProfileType.GUEST) {
                try {
                    emailModelProvider.appendToken(model, jwtTokenService.generateGuestUserToken(application));
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        Object[] subjectFormat = new String[]{property.getData().getName()};

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(
                userProfile.getId(), template, subject, subjectFormat, model, property.getCustomer().getId());
        sendPropertySearcherEmailMessage(container, userProfile);
    }

    private void sendAppointmentChangeNotification(Appointment appointment,
            PropertySearcherUserProfile userProfile, Map<String, File> attachmentFiles) {
        Property property = appointment.getProperty();

        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(userProfile, property);
        emailModelProvider.appendFormattedDate(appointment.getDate(), model,
                ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);
        model.put(ModelParams.MODEL_APPOINTMENT, appointment);

        //TODO Attachment Upload einbauen
        Map<String, S3File> attachments = new HashMap<>();

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(userProfile.getId(),
                MailTemplate.VIEWING_CHANGED, VIEWING_CHANGED_SUBJECT_KEY, model, property.getCustomer().getId());

        sendPropertySearcherEmailMessage(container, userProfile);
    }

    private void sendInviteToViewingNotification(Appointment appointment, String email, Map<String, File> attachmentFiles) {
        Property property = appointment.getProperty();

        Map<String, Object> model = emailModelProvider.createPropertyModel(property);
        emailModelProvider.appendFormattedDate(appointment.getDate(), model,
                ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);

        //TODO Attachment Upload einbauen
        Map<String, S3File> attachments = new HashMap<>();

        PropertySearcherProfileMailBrokerContainer propertySearcherMailBrokerContainer = new PropertySearcherProfileMailBrokerContainer(
                property.getCustomer().getId(), email, MailTemplate.INVITED_TO_VIEWING,
                INVITE_TO_VIEWING_SUBJECT_KEY, model, attachments);

        sendPropertySearcherEmailMessage(propertySearcherMailBrokerContainer);
    }

    private void addInviteIcsToAttachments(Map<String, File> attachments, Appointment appointment) throws
            IOException {
        File invite = getICalFile(appointment);
        attachments.put(ICalEventHandler.INVITE_ICS_FILE_NAME, invite);
    }

    private File getICalFile(Appointment appointment) throws IOException {
        ICalEventHandler iCalEventHandler = new ICalEventHandler();
        return iCalEventHandler.createEvent(
                appointment.getId().toString(),
                ICalEventHandler.ICAL_TITLE,
                appointment.getProperty().getData().getAddress().toString(),
                ICalEventHandler.LANGUAGE,
                appointment.getDate(),
                durationInMinutes
        );
    }
}
