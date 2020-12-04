package de.immomio.landlord.service.user;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LandlordChangeEmailService {

    public static final String EMAIL_CHANGE_SUBJECT = "email.change.subject";

    public static final String EMAIL_CHANGED_SUBJECT = "email.changed.subject";

    private final LandlordMailSender mailSender;

    @Autowired
    public LandlordChangeEmailService(LandlordMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendChangeEmailNotification(LandlordUser user, ChangeEmail changeEmail) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        userMap.put(ModelParams.MODEL_TOKEN, changeEmail.getToken());
        userMap.put(ModelParams.MODEL_NEW_EMAIL, changeEmail.getEmail());
        userMap.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);

        mailSender.send(user.getEmail(), user, MailTemplate.NEW_EMAIL, EMAIL_CHANGE_SUBJECT, userMap);
    }

    public void sendEmailChangedNotification(LandlordUser user, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(ModelParams.MODEL_USER, new LandlordUserBean(user));

        mailSender.send(email, user, MailTemplate.NEW_EMAIL_CHANGED, EMAIL_CHANGED_SUBJECT, userMap);
    }

}
