package de.immomio.service.landlord;

import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserBean;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.service.landlord.customer.user.changepassword.LandlordChangePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.immomio.mail.ModelParams.MODEL_CUSTOMER;
import static de.immomio.mail.ModelParams.MODEL_IGNORE_DISCLAIMER;
import static de.immomio.mail.ModelParams.MODEL_TOKEN;
import static de.immomio.mail.ModelParams.MODEL_USER;

@Service
public class LandlordChangePasswordService extends AbstractChangePasswordService {

    private final LandlordMailSender mailSender;

    private final LandlordUserRepository userRepository;

    private final LandlordChangePasswordRepository changePasswordRepository;

    private static final String NEW_AGENT_CHANGE_PASSWORD_SUBJECT_KEY = "commercial.new.agent.subject";

    private static final String NEW_PROPERTY_MANAGER_CHANGE_PASSWORD_SUBJECT_KEY = "commercial.new.property.manager.subject";

    @Autowired
    public LandlordChangePasswordService(
            LandlordMailSender mailSender,
            LandlordUserRepository userRepository,
            LandlordChangePasswordRepository changePasswordRepository
    ) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.changePasswordRepository = changePasswordRepository;
    }

    public void resetPassword(String email) {
        if (email == null || email.isEmpty()) {
            return;
        }

        LandlordUser user = userRepository.findByEmail(email);

        resetPassword(user);
    }

    private void resetPassword(LandlordUser user) {
        if (user == null) {
            return;
        }

        ChangePassword cp = createChangePassword(user);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", new LandlordUserBean(user));
        userMap.put("token", cp.getToken());

        mailSender.send(user, MailTemplate.RESET_PASSWORD, getPasswordResetSubjectKey(), userMap);

    }

    public void newUser(LandlordUser user) {
        if (user == null) {
            return;
        }

        ChangePassword cp = createChangePassword(user);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put(MODEL_USER, new LandlordUserBean(user));
        userMap.put(MODEL_CUSTOMER, new LandlordCustomerBean(user.getCustomer(), user.fullName()));
        userMap.put(MODEL_TOKEN, cp.getToken());
        userMap.put(MODEL_IGNORE_DISCLAIMER, true);

        MailTemplate template = user.getUsertype() == LandlordUsertype.PROPERTYMANAGER ? MailTemplate.COMMERCIAL_NEW_PROPERTY_MANAGER : MailTemplate.COMMERCIAL_NEW_AGENT;
        String subject = user.getUsertype() == LandlordUsertype.PROPERTYMANAGER ? NEW_PROPERTY_MANAGER_CHANGE_PASSWORD_SUBJECT_KEY : NEW_AGENT_CHANGE_PASSWORD_SUBJECT_KEY;
        mailSender.send(user, template, subject, userMap);
    }

    private ChangePassword createChangePassword(LandlordUser user) {
        ChangePassword cp = new ChangePassword();

        cp.setToken(UUID.randomUUID().toString());
        cp.setUser(user);

        cp = changePasswordRepository.save(cp);

        return cp;
    }
}
