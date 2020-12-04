package de.immomio.service.followup;

import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.followup.Followup;
import de.immomio.data.landlord.entity.property.followup.FollowupNotification;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.landlord.customer.property.followup.BaseFollowupNotificationRepository;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */
@Service
public class FollowupNotificationService {

    private final BaseFollowupNotificationRepository followupNotificationRepository;

    private final LandlordMailSender mailSender;

    private final EmailModelProvider emailModelProvider;

    @Autowired
    public FollowupNotificationService(BaseFollowupNotificationRepository followupNotificationRepository, LandlordMailSender mailSender, EmailModelProvider emailModelProvider) {
        this.followupNotificationRepository = followupNotificationRepository;
        this.mailSender = mailSender;
        this.emailModelProvider = emailModelProvider;
    }

    public void notifyUsers() {
        ZonedDateTime zonedTo = ZonedDateTime.now();
        ZonedDateTime zonedFrom = zonedTo.minusMinutes(5);

        List<FollowupNotification> followupNotifications = followupNotificationRepository.findAllForNotification(Date.from(zonedFrom.toInstant()), Date.from(zonedTo.toInstant()));
        followupNotifications.stream()
                .map(FollowupNotification::getFollowup)
                .forEach(this::notifyUser);

        setRead(followupNotifications);
    }

    private void notifyUser(Followup followup) {
        Property property = followup.getProperty();
        LandlordUser user = property.getUser();
        Map<String, Object> params = new HashMap<>();
        params.put(ModelParams.MODEL_CONTRACT_PROPERTY_DATA, new PropertyMailBean(property));
        params.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        params.put(ModelParams.MODEL_FOLLOWUP, new FollowupBean(followup));
        emailModelProvider.appendFormattedDate(followup.getDate(), params, ModelParams.MODEL_FOLLOWUP_DATE_FORMATTED);
        mailSender.send(user, MailTemplate.FOLLOWUP_NOTIFICATION, "followup.notification.subject", params);
    }

    private void setRead(List<FollowupNotification> notifications) {
        if(!notifications.isEmpty()) {
            followupNotificationRepository.setRead(notifications);
        }
    }

}
