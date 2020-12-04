package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.propertysearcher.entity.user.ProfileCompletenessResponseBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.PropertySearcherMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.service.propertysearcher.PropertySearcherUserService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 */
@Slf4j
@Service
public class IncompleteProfileTask extends BaseTask {

    private static final int HUNDRED = 100;
    private static final String SUBJECT = "profile.completeness.reminder";
    private static final double MAX_COMPLETION_PERCENTAGE = 70.0;
    private static final double MIN_COMPLETION_PERCENTAGE = 0.0;

    private final PropertySearcherMailSender propertySearcherMailSender;

    private final BasePropertySearcherUserRepository userRepository;

    private final BasePropertySearcherUserProfileRepository userProfileRepository;

    private final PropertySearcherUserService userService;

    @Autowired
    public IncompleteProfileTask(PropertySearcherMailSender propertySearcherMailSender,
            BasePropertySearcherUserRepository userRepository,
            BasePropertySearcherUserProfileRepository userProfileRepository,
            PropertySearcherUserService userService) {
        this.propertySearcherMailSender = propertySearcherMailSender;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.userService = userService;
    }

    @Override
    public boolean run() {
        checkIncompleteProfiles();

        return true;
    }

    private void checkIncompleteProfiles() {
        DateTime now = DateTime.now();
        Date oneHourBefore = now.minusHours(1).toDate();
        Date twoHoursBefore = now.minusHours(2).toDate();

        List<PropertySearcherUser> registeredUsers = userRepository.findByTypeAndCreatedBetween(PropertySearcherUserType.REGISTERED, twoHoursBefore, oneHourBefore);

        registeredUsers.stream().map(PropertySearcherUser::getMainProfile).filter(this::userShouldBeReminded).forEach(this::remindUser);
    }

    private void remindUser(PropertySearcherUserProfile userProfile) {
        Map<String, Object> model = new HashMap<>();
        model.put(ModelParams.MODEL_USER, new PropertySearcherUserProfileEmailBean(userProfile));
        model.put(ModelParams.MODEL_USER_PROFILE, userProfile.getData());
        propertySearcherMailSender.send(userProfile, MailTemplate.INCOMPLETE_PROFILE, SUBJECT, model);
    }

    private boolean userShouldBeReminded(PropertySearcherUserProfile userProfile) {
        ProfileCompletenessResponseBean profileCompletenessResponseBean = userService.calculateProfileCompleteness(userProfile);
        double completenessPercentage = getCompletenessPercentage(profileCompletenessResponseBean);

        return userProfile.getType() != PropertySearcherUserProfileType.GUEST && isPercentageMet(completenessPercentage);
    }

    private boolean isPercentageMet(double completenessPercentage) {
        return completenessPercentage >= MIN_COMPLETION_PERCENTAGE && completenessPercentage <= MAX_COMPLETION_PERCENTAGE;
    }

    private double getCompletenessPercentage(ProfileCompletenessResponseBean profileCompletenessResponseBean) {
        return profileCompletenessResponseBean.getCompletenessPercentage() * HUNDRED;
    }
}
