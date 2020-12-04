package de.immomio.service.appointment;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.shared.appointment.acceptance.BaseAppointmentAcceptanceRepository;
import de.immomio.security.service.JWTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Service
public class AppointmentSendMailService {

    private static final int TOKEN_TTL_DAYS = 3;

    private PropertySearcherMailSender mailSender;

    private PropertySearcherMailConfigurator mailConfigurator;

    private final JWTTokenService jwtTokenService;

    private final BaseAppointmentAcceptanceRepository appointmentAcceptanceRepository;

    private static final String INTENT_REMINDER_SUBJECT = "intent.reminder.subject";
    private static final String GUEST_MODE_INTENT_REMINDER_SUBJECT = "guest_mode_intent_reminder.subject";

    @Autowired
    public AppointmentSendMailService(
            PropertySearcherMailSender mailSender,
            PropertySearcherMailConfigurator mailConfigurator,
            JWTTokenService jwtTokenService,
            BaseAppointmentAcceptanceRepository appointmentAcceptanceRepository
    ) {
        this.mailSender = mailSender;
        this.mailConfigurator = mailConfigurator;
        this.jwtTokenService = jwtTokenService;
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
    }

    public void sendIntentMails(Appointment appointment) {
        List<AppointmentAcceptance> appointmentAcceptances =
                appointmentAcceptanceRepository.findByAppointmentAndStateAndEmailSent(
                        appointment,
                        AppointmentAcceptanceState.ACTIVE, false);
        LandlordCustomer customer = appointment.getProperty().getCustomer();
        appointmentAcceptances.forEach(appointmentAcceptance -> {
            try {
                PropertyApplication application = appointmentAcceptance.getApplication();
                if (application.isAskedForIntent() ||
                        application.getStatus() == ApplicationStatus.INTENT ||
                        application.getStatus() == ApplicationStatus.NO_INTENT) {
                    return;
                }
                PropertySearcherUserProfile userProfile = application.getUserProfile();

                PropertySearcherUserProfileType type = userProfile.getType();
                String token = type == PropertySearcherUserProfileType.GUEST ? jwtTokenService.generateGuestUserToken(application)
                        : jwtTokenService.generateApplicationIntentToken(application, TOKEN_TTL_DAYS);
                MailTemplate template = type == PropertySearcherUserProfileType.GUEST ? MailTemplate.GUEST_INTENT_REMINDER : MailTemplate.INTENT_REMINDER;
                String subject = type == PropertySearcherUserProfileType.GUEST ? GUEST_MODE_INTENT_REMINDER_SUBJECT :INTENT_REMINDER_SUBJECT;

                Map<String, Object> model = getIntentMailModel(application, token);

                mailSender.send(userProfile,
                        template,
                        subject,
                        model,
                        customer.getId());
                setEmailSent(appointmentAcceptance);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void setEmailSent(AppointmentAcceptance appointmentAcceptance) {
        appointmentAcceptance.setEmailSent(true);
        appointmentAcceptanceRepository.save(appointmentAcceptance);
    }

    private Map<String, Object> getIntentMailModel(PropertyApplication application, String token) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(application.getUserProfile()));
        model.put(ModelParams.MODEL_TOKEN, token);
        model.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());
        model.put(ModelParams.MODEL_APPLICATION, application.getId());
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(application.getProperty()));
        model.put(ModelParams.SUBJECT_PLACEHOLDER, application.getProperty().getData().getName());

        return model;
    }
}
