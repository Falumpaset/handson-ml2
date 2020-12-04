package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseLandlordTask;
import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.configurator.PropertySearcherMailConfigurator;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.landlord.customer.user.BaseLandlordUserRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.security.service.JWTTokenService;
import de.immomio.security.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DisableUserWithoutVerifiedEmailTask extends BaseLandlordTask {

    @Value("${email.period.verificationInDays}")
    private int emailVerificationPeriod;

    @Value("${email.period.lockWarningInDays}")
    private int lockWarningPeriod;

    private final int ONE_DAY = 1;
    private static final int SEVEN_DAYS = 7;
    private static final int FOUR_DAYS = 4;
    private static final String PROPERTYSEARCHER_ACCOUNT_LOCKED_SUBJECT = "propertysearcher.account.locked";
    private static final String PROPERTYSEARCHER_VERIFICATION_REQUIRED_WARNING = "propertysearcher.account.verification.required";

    private final BaseLandlordUserRepository baseLandlordUserRepository;

    private final BasePropertySearcherUserRepository basePropertySearcherUserRepository;

    private final PropertySearcherMailSender mailSender;

    private final JWTTokenService jwtTokenService;

    private final PropertySearcherMailConfigurator mailConfigurator;

    private final KeycloakService keycloakService;

    @Autowired
    public DisableUserWithoutVerifiedEmailTask(
            BaseLandlordUserRepository baseLandlordUserRepository,
            BasePropertySearcherUserRepository basePropertySearcherUserRepository,
            PropertySearcherMailSender mailSender,
            JWTTokenService jwtTokenService,
            PropertySearcherMailConfigurator mailConfigurator,
            KeycloakService keycloakService
    ) {
        this.baseLandlordUserRepository = baseLandlordUserRepository;
        this.basePropertySearcherUserRepository = basePropertySearcherUserRepository;
        this.mailSender = mailSender;
        this.jwtTokenService = jwtTokenService;
        this.mailConfigurator = mailConfigurator;
        this.keycloakService = keycloakService;
    }

    @Override
    public boolean run() {
        deactivateUsersWithoutEmailVerification();
        warnUsersWithoutEmailVerification();

        return true;
    }

    private void deactivateUsersWithoutEmailVerification() {
        log.info("Starting users disabling without email verification...");
        DateTime emailVerificationPeriodDate = DateTime.now().minusDays(emailVerificationPeriod);
        Date startDate = emailVerificationPeriodDate.toDate();
        Date endDate = emailVerificationPeriodDate.plusDays(ONE_DAY).toDate();

        baseLandlordUserRepository.findAllNotEmailVerified(startDate, endDate).forEach(user -> {
            user.setEnabled(false);
            deactivateSsoUser(user);
            baseLandlordUserRepository.save(user);
        });

        basePropertySearcherUserRepository.findAllNotEmailVerified(startDate, endDate)
                .forEach(user -> {
                    user.setEnabled(false);
                    deactivateSsoUser(user);
                    notifyUser(user.getMainProfile());
                    basePropertySearcherUserRepository.save(user);
                });
    }

    private void warnUsersWithoutEmailVerification() {
        log.info("Starting warning users without email verification...");
        DateTime accountLockWarningPeriodDate = DateTime.now().minusDays(lockWarningPeriod);
        Date startDate = accountLockWarningPeriodDate.toDate();
        Date endDate = accountLockWarningPeriodDate.plusDays(ONE_DAY).toDate();

        basePropertySearcherUserRepository.findAllNotEmailVerified(startDate, endDate)
                .forEach(this::warnUser);
    }

    private void deactivateSsoUser(AbstractUser user) {
        List<UserRepresentation> users = keycloakService.searchUser(user.getEmail());
        if (users.size() == 1) {
            UserRepresentation userRepresentation = users.get(0);
            userRepresentation.setEnabled(false);
            keycloakService.updateUser(userRepresentation);
        } else {
            log.warn("User " + user.getEmail() + "not found in SSO;");
        }
    }

    private void notifyUser(PropertySearcherUserProfile userProfile) {
        try {
            Map<String, Object> accountLockedModel = getEmailVerificationModel(userProfile, SEVEN_DAYS);

            mailSender.send(userProfile,
                    MailTemplate.ACCOUNT_LOCKED_NOTIFICATION,
                    PROPERTYSEARCHER_ACCOUNT_LOCKED_SUBJECT,
                    accountLockedModel);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void warnUser(PropertySearcherUser user) {
        try {
            Map<String, Object> warningModel = getEmailVerificationModel(user.getMainProfile(), FOUR_DAYS);

            mailSender.send(user.getMainProfile(),
                    MailTemplate.VERIFICATION_REQUIRED_WARNING,
                    PROPERTYSEARCHER_VERIFICATION_REQUIRED_WARNING,
                    warningModel);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Map<String, Object> getEmailVerificationModel(PropertySearcherUserProfile userProfile, int days) throws IOException {
        Map<String, Object> model = new HashMap<>();

        String token = jwtTokenService.generateEmailToken(userProfile.getUser(), days);
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_TOKEN, token);
        model.put(ModelParams.RETURN_URL, mailConfigurator.buildAppUrl());

        return model;
    }
}
