package de.immomio.landlord.service.followup;

import de.immomio.data.landlord.entity.property.followup.bean.FollowupNotificationBean;
import de.immomio.landlord.service.user.settings.LandlordUserSettingsService;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * @author Fabian Beck
 */

public class FollowupIntervalSettingsServiceTest extends AbstractTest {

    @Mock
    private LandlordUserSettingsService userSettingsService;

    @Captor
    private ArgumentCaptor<List<Long>> userSettingsIntervalsCaptor;

    @InjectMocks
    private FollowupIntervalSettingsService followupIntervalSettingsService;

    @Test
    public void saveFollowupIntervals(){
        ZonedDateTime followupTime = ZonedDateTime.now();
        Date followupDate = Date.from(followupTime.toInstant());
        Date notificationDateOne = Date.from(followupTime.minusHours(1).toInstant());
        Date notificationDateTwo = Date.from(followupTime.minusDays(1).toInstant());

        List<FollowupNotificationBean> notificationBeans = new ArrayList<>();
        notificationBeans.add(FollowupNotificationBean.builder().date(notificationDateOne).build());
        notificationBeans.add(FollowupNotificationBean.builder().date(notificationDateTwo).build());

        followupIntervalSettingsService.saveFollowupIntervals(followupDate, notificationBeans);

        verify(userSettingsService).saveFollowupIntervals(userSettingsIntervalsCaptor.capture());
        List<Long> intervals = userSettingsIntervalsCaptor.getValue();
        Long diffOne = followupDate.getTime() - notificationDateOne.getTime();
        Assert.assertEquals(diffOne, intervals.get(0));
        Long diffTwo = followupDate.getTime() - notificationDateTwo.getTime();
        Assert.assertEquals(diffTwo, intervals.get(1));
    }
}
