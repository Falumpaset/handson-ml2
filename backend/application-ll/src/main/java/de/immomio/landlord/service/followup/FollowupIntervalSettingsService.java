package de.immomio.landlord.service.followup;

import de.immomio.data.landlord.entity.property.followup.bean.FollowupNotificationBean;
import de.immomio.landlord.service.user.settings.LandlordUserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck
 */

@Service
public class FollowupIntervalSettingsService {

    private LandlordUserSettingsService userSettingsService;

    @Autowired
    public FollowupIntervalSettingsService(LandlordUserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    public void saveFollowupIntervals(Date finalDate, List<FollowupNotificationBean> notificationBeans) {
        List<Long> intervals = notificationBeans.stream()
                .map(bean -> finalDate.getTime() - bean.getDate().getTime())
                .collect(Collectors.toList());

        userSettingsService.saveFollowupIntervals(intervals);
    }

}
