package de.immomio.service.propertysearcher;

import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.security.service.JWTTokenService;
import de.immomio.service.shared.EmailModelProvider;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertySearcherRefreshService {

    private static final String SEARCHING_REMINDER_SUBJECT = "email.searching.subject";

    private final JWTTokenService jwtTokenService;

    private final PropertySearcherMailSender mailSender;

    private final EmailModelProvider emailModelProvider;

    @Value("${email.noAuth.tokenExpirationInDays}")
    private int tokenExpirationInDays;

    @Autowired
    public PropertySearcherRefreshService(
            JWTTokenService jwtTokenService,
            PropertySearcherMailSender mailSender,
            EmailModelProvider emailModelProvider
    ) {
        this.jwtTokenService = jwtTokenService;
        this.mailSender = mailSender;
        this.emailModelProvider = emailModelProvider;
    }

    public void sendSearchInquiryEmails(List<PropertySearcherUserProfile> userProfiles) {
        userProfiles.stream().filter(userProfile -> userProfile.getType() != PropertySearcherUserProfileType.GUEST).forEach(userProfile -> {
            try {
                String generateEmailToken = jwtTokenService.generateEmailToken(userProfile.getUser(), tokenExpirationInDays);
                Map<String, Object> model = emailModelProvider.createPsMailModel(userProfile, generateEmailToken);
                if (userProfile.isInInternalPool() == Boolean.TRUE) {
                    emailModelProvider.appendCustomerBranding(model, userProfile.getTenantPoolCustomer());
                }

                mailSender.send(userProfile,
                        MailTemplate.USER_SEARCHING_INQUIRY,
                        SEARCHING_REMINDER_SUBJECT,
                        model);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

}
