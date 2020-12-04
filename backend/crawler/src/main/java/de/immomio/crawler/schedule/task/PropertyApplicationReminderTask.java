package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Component
public class PropertyApplicationReminderTask extends BaseTask {

    @Value("${email.renter}")
    public String fromEmail;

    private final BasePropertyApplicationRepository propertyApplicationRepository;

    private final BasePropertyRepository propertyRepository;

    private final LandlordMailSender mailSender;

    @Autowired
    public PropertyApplicationReminderTask(
            BasePropertyApplicationRepository propertyApplicationRepository,
            BasePropertyRepository propertyRepository,
            LandlordMailSender mailSender
    ) {
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.propertyRepository = propertyRepository;
        this.mailSender = mailSender;
    }

    @Override
    public boolean run() {
        applicants();

        return false;
    }

    private void applicants() {
        try {
            propertyRepository.findAllByState(PropertyPortalState.ACTIVE, null).forEach(this::olderThanHours);
        } catch (Exception ex) {
            log.info("Exception thrown during crawling", ex);
        }
    }

    private void olderThanHours(Property property) {
        DateTime tMinus24 = new DateTime().minusHours(24);
        DateTime tMinus48 = new DateTime().minusHours(48);

        List<LandlordUser> users = isAnonymousAndShouldBeNotified(tMinus24.toDate(), tMinus48.toDate(), property);
        sendUpdate(property, users, tMinus24.toDate());
    }

    private List<LandlordUser> isAnonymousAndShouldBeNotified(Date before, Date after, Property property) {
        List<LandlordUser> users = new ArrayList<>();
        List<PropertyApplication> propertyApplications = propertyApplicationRepository
                .findAllByPropertyAndCreatedBetween(property, after, before);

        for (PropertyApplication propertyApplication : propertyApplications) {
            LandlordUser user = getPropertyUser(propertyApplication.getProperty());
            if (user != null && !user.isEnabled() && user.getLastLogin() == null) {
                users.add(user);
            }
        }

        return users;
    }

    private void sendUpdate(Property property, List<LandlordUser> users, Date before) {
        users.forEach(user -> {
            Map<String, Object> model = geApplicationReminderModel(property, user, before);
            mailSender.send(
                    user,
                    MailTemplate.APPLICATION_REMINDER,
                    getApplicationReminderSubject(property),
                    model,
                    fromEmail);
        });
    }

    private String getApplicationReminderSubject(Property property) {
        return "Re: Besichtigungstermin für: " + property.getData().getName();
    }

    private Map<String, Object> geApplicationReminderModel(Property property, LandlordUser user, Date before) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_FLAT, new PropertyMailBean(property));
        model.put(ModelParams.MODEL_APPLICATION_DATE, before);

        LandlordCustomer landlordCustomer = property.getCustomer();
        model.put(ModelParams.MODEL_PREFERENCES, landlordCustomer.getPreferences());
        model.put(ModelParams.MODEL_BRANDING_LOGO, landlordCustomer.getBrandingLogo());

        return model;
    }

}
