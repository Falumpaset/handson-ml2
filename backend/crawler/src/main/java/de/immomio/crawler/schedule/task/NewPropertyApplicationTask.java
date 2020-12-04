package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
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
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Component
public class NewPropertyApplicationTask extends BaseTask {

    private static final String PROPERTY_UPDATE_MAIL_SUBJECT = "application.update.subject";

    private final BasePropertyApplicationRepository propertyApplicationRepository;

    private final BasePropertyRepository propertyRepository;

    private final BaseLandlordCustomerRepository customerRepository;

    private final LandlordMailSender mailSender;

    @Autowired
    public NewPropertyApplicationTask(
            BasePropertyApplicationRepository propertyApplicationRepository,
            BasePropertyRepository propertyRepository,
            BaseLandlordCustomerRepository customerRepository,
            LandlordMailSender mailSender
    ) {
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.propertyRepository = propertyRepository;
        this.customerRepository = customerRepository;
        this.mailSender = mailSender;
    }

    @Override
    public boolean run() {
        newApplicants();

        return true;
    }

    private void newApplicants() {
        List<LandlordCustomer> customers = customerRepository.findAll();

        try {
            customers.forEach(customer -> {
                Map<LandlordUser, List<Property>> propertyNotifications = new HashMap<>();
                DateTime before = new DateTime();
                DateTime after = before.minusHours(24);

                propertyRepository.findByCustomerAndState(customer, PropertyPortalState.ACTIVE, null)
                        .forEach(property -> {
                            LandlordUser user = getPropertyUser(property);
                            if (user != null) {
                                List<PropertyApplication> propertyApplications = propertyApplicationRepository
                                        .findAllByPropertyAndCreatedBetween(property, after.toDate(), before.toDate());

                                if (!propertyApplications.isEmpty()) {
                                    propertyNotifications.computeIfAbsent(user, k -> new ArrayList<>())
                                            .add(property);
                                }
                            }
                        });

                for (Map.Entry<LandlordUser, List<Property>> entry : propertyNotifications.entrySet()) {
                    if (!entry.getValue().isEmpty()) {
                        sendUpdate(entry.getValue(), entry.getKey());
                    }
                }
            });
        } catch (Exception ex) {
            log.info("Exception thrown during crawling", ex);
        }
    }

    private void sendUpdate(List<Property> properties, LandlordUser user) {
        Map<String, Object> model = getPropertyUpdateModel(properties, user);
        mailSender.send(user.getEmail(), MailTemplate.APPLICATION_DAILY_UPDATE, PROPERTY_UPDATE_MAIL_SUBJECT, model);
    }

    private Map<String, Object> getPropertyUpdateModel(List<Property> properties, LandlordUser user) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new LandlordUserBean(user));
        model.put(ModelParams.MODEL_FLATS, properties.stream().map(PropertyMailBean::new).collect(Collectors.toList()));
        model.put(ModelParams.MODEL_IGNORE_DISCLAIMER, true);
        model.put(ModelParams.MODEL_CUSTOMER, new LandlordCustomerBean(user.getCustomer(), user.fullName()));

        return model;
    }

}
