package de.immomio.landlord.service.product;

import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.price.LandlordPrice;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.service.landlord.AbstractNotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LandlordCheckoutNotificationService extends AbstractNotificationService {

    private static final String ADDON_HOMEPAGE_MODULE_ADDED_SUBJECT_KEY = "addon.homepage.module.added.subject";

    private static final String ADDON_HOMEPAGE_MODULE_REMOVED_SUBJECT_KEY = "addon.homepage.module.removed.subject";

    @Value("${email.homepage.module}")
    private String homepageModuleEmail;

    private final LandlordMailSender mailSender;

    @Autowired
    public LandlordCheckoutNotificationService(LandlordMailSender mailSender, RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
        this.mailSender = mailSender;
    }

    public void homepageModuleAdded(LandlordCustomer customer, LandlordUser user, LandlordPrice price) {
        LandlordCustomerBean customerBean = getLandlordCustomerBean(customer, user);
        Map<String, Object> model = createHomepageModuleModel(customerBean, new LandlordUserBean(user), price);
        mailSender.send(homepageModuleEmail, user, customer, MailTemplate.COMMERCIAL_ADD_HOMEPAGE_MODULE,
                ADDON_HOMEPAGE_MODULE_ADDED_SUBJECT_KEY, model);
    }

    public void homepageModuleRemoved(LandlordCustomer customer, LandlordUser user) {
        LandlordCustomerBean customerBean = getLandlordCustomerBean(customer, user);
        Map<String, Object> model = createHomepageModuleModel(customerBean, new LandlordUserBean(user), null);
        Object[] subjectFormat = new String[]{customerBean.getName()};

        mailSender.send(homepageModuleEmail, user, customer, MailTemplate.COMMERCIAL_REMOVE_HOMEPAGE_MODULE,
                ADDON_HOMEPAGE_MODULE_REMOVED_SUBJECT_KEY, subjectFormat, model);
    }

    private Map<String, Object> createHomepageModuleModel(LandlordCustomerBean customerBean, LandlordUserBean userBean,
                                                          LandlordPrice price) {
        Map<String, Object> model = new HashMap<>();

        model.put(ModelParams.MODEL_CUSTOMER, customerBean);
        model.put(ModelParams.MODEL_USER, userBean);
        if (price != null) {
            model.put(ModelParams.MODEL_SETUP_PRICE, price.getSetup());
        }

        return model;
    }

    private LandlordCustomerBean getLandlordCustomerBean(LandlordCustomer customer, LandlordUser user) {
        return new LandlordCustomerBean(customer, user.fullName());
    }
}
