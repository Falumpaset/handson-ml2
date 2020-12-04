package de.immomio.service.appointment;

import de.immomio.common.ical.ICalEventHandler;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentBean;
import de.immomio.data.shared.entity.appointment.attendance.AppointmentAttendance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.messaging.container.mail.LandlordMailBrokerContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.shared.EmailModelProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class AppointmentNotificationService {

    private static final String APPOINTMENT_ACCEPTED_SUBJECT_KEY = "invitation.accepted.subject";

    private static final String APPOINTMENT_ACCEPTANCE_SUBJECT_KEY = "invitation.acceptance.notification.subject";

    private static final String APPOINTMENT_CANCELLATION_SUBJECT_KEY = "invitation.cancellation.notification.subject";

    private static final String EXCLUSIVE_APPOINTMENT_CANCELLATION_SUBJECT_KEY = "exclusive.invitation.cancellation.notification.subject";

    private static final String CANCEL_ICS = "cancel.ics";

    private static final String INVITE_ICS = "invite.ics";

    private final RabbitTemplate rabbitTemplate;

    private final PropertySearcherMailSender mailSender;

    private final EmailModelProvider emailModelProvider;

    private final JWTTokenService jwtTokenService;

    @Value("${appointment.durationInMinutes}")
    private int durationInMinutes;

    public AppointmentNotificationService(PropertySearcherMailSender mailSender,
            RabbitTemplate rabbitTemplate,
            EmailModelProvider emailModelProvider,
            JWTTokenService jwtTokenService) {
        this.mailSender = mailSender;
        this.rabbitTemplate = rabbitTemplate;
        this.emailModelProvider = emailModelProvider;
        this.jwtTokenService = jwtTokenService;
    }

    public void appointmentAccepted(AppointmentAcceptance appointmentAcceptance) {
        PropertyApplication application = appointmentAcceptance.getApplication();
        PropertySearcherUserProfile userProfile = application.getUserProfile();
        Map<String, Object> model = createAppointmentAttendanceModel(appointmentAcceptance);

        Appointment appointment = appointmentAcceptance.getAppointment();
        Map<String, String> attachments = icsAttachment(
                appointment,
                AppointmentAcceptanceState.ACTIVE);

        // notify participant
        Property property = application.getProperty();
        mailSender.send(
                userProfile,
                MailTemplate.INVITATION_ACCEPTED,
                APPOINTMENT_ACCEPTED_SUBJECT_KEY,
                new String[]{property.getData().getName()},
                model,
                property.getCustomer().getId(),
                attachments);

        // notify landlord
        if (appointment.getAppointmentAcceptances().isEmpty()) {
            LandlordUser landlordUser = getLandlordMailRecipient(appointment);
            model = createLandlordAppointmentAcceptanceModel(landlordUser, appointmentAcceptance);
            LandlordMailBrokerContainer container = new LandlordMailBrokerContainer(
                    getLandlordMailRecipient(appointment).getId(),
                    MailTemplate.INVITATION_ACCEPTANCE_NOTIFICATION,
                    APPOINTMENT_ACCEPTANCE_SUBJECT_KEY,
                    model
            );
            sendLandlordEmailMessage(container);
        }

    }

    public void appointmentCanceled(AppointmentAcceptance appointmentAcceptance) {
        Appointment appointment = appointmentAcceptance.getAppointment();
        Map<String, String> attachments = icsAttachment(appointment,
                AppointmentAcceptanceState.CANCELED);
        Map<String, Object> model = createAppointmentAttendanceModel(appointmentAcceptance);
        LandlordMailBrokerContainer container;

        if (appointment.isExclusive()) {
            container = new LandlordMailBrokerContainer(
                    getLandlordMailRecipient(appointment).getId(),
                    MailTemplate.EXCLUSIVE_INVITATION_CANCELED_TO_LL,
                    APPOINTMENT_CANCELLATION_SUBJECT_KEY,
                    model,
                    attachments
            );
        } else {
            container = new LandlordMailBrokerContainer(
                    getLandlordMailRecipient(appointment).getId(),
                    MailTemplate.INVITATION_DECLINE_NOTIFICATION,
                    APPOINTMENT_CANCELLATION_SUBJECT_KEY,
                    model,
                    attachments
            );

        }

        sendLandlordEmailMessage(container);
    }

    private Map<String, String> icsAttachment(Appointment appointment, AppointmentAcceptanceState state) {
        Map<String, String> attachments = new HashMap<>();
        ICalEventHandler iCalEventHandler = new ICalEventHandler();

        try {
            String title = ICalEventHandler.ICAL_TITLE;
            if (state == AppointmentAcceptanceState.ACTIVE) {
                File file = iCalEventHandler.createEvent(appointment.getId().toString(), title,
                        appointment.getProperty().getData().getAddress().toString(), ICalEventHandler.LANGUAGE,
                        appointment.getDate(),
                        durationInMinutes);
                attachments.put(INVITE_ICS, file.getAbsolutePath());
            } else if (state == AppointmentAcceptanceState.CANCELED) {
                File file = iCalEventHandler.cancelEvent(appointment.getId().toString(), title);
                attachments.put(CANCEL_ICS, file.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("Could not created ICS attachment", e);
        }

        return attachments;
    }

    private Map<String, Object> createAppointmentAttendanceModel(AppointmentAttendance applicationAssociation) {
        PropertySearcherUserProfile userProfile = applicationAssociation.getApplication().getUserProfile();
        Property property = applicationAssociation.getApplication().getProperty();

        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property));
        model.put(ModelParams.MODEL_APPOINTMENT, new AppointmentBean(applicationAssociation.getAppointment()));
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);
        emailModelProvider.appendFormattedDate(applicationAssociation.getAppointment().getDate(), model, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);

        if (userProfile.getType() == PropertySearcherUserProfileType.GUEST) {
            emailModelProvider.appendToken(model, getGuestUserToken(applicationAssociation.getApplication()));
        }

        populateCustomerPreferencesAndAccessing(model, property.getCustomer());

        return model;
    }

    private String getGuestUserToken(PropertyApplication application) {
        try {
            return jwtTokenService.generateGuestUserToken(application);
        } catch (IOException e) {
            throw new ImmomioRuntimeException("TOKEN_GENERATION_FAILED_L");
        }
    }

    private Map<String, Object> createLandlordAppointmentAcceptanceModel(
            LandlordUser user,
            AppointmentAcceptance appointmentAcceptance
    ) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_USER_PROFILE, user.getProfile());
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(appointmentAcceptance.getApplication().getProperty()));
        model.put(ModelParams.MODEL_APPOINTMENT, new AppointmentBean(appointmentAcceptance.getAppointment()));
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);
        emailModelProvider.appendFormattedDate(appointmentAcceptance.getAppointment().getDate(), model, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);
        populateCustomerPreferencesAndAccessing(model, user.getCustomer());

        return model;
    }

    private void sendLandlordEmailMessage(LandlordMailBrokerContainer messageBean) {
        rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(
                QueueConfigUtils.MailLandlordBrokerConfig.EXCHANGE_NAME,
                QueueConfigUtils.MailLandlordBrokerConfig.ROUTING_KEY,
                messageBean);
    }

    private void populateCustomerPreferencesAndAccessing(Map<String, Object> model, LandlordCustomer customer) {
        model.put(ModelParams.MODEL_PREFERENCES, customer.getPreferences());
        model.put(ModelParams.MODEL_ALLOW_BRANDING, customer.isBrandingAllowed());
        model.put(ModelParams.MODEL_BRANDING_LOGO, customer.getBrandingLogo());
    }

    private LandlordUser getLandlordMailRecipient(Appointment appointment) {
        Property property = appointment.getProperty();
        return property.getPropertyManager() != null ?
                property.getPropertyManager() :
                property.getUser();
    }

}
