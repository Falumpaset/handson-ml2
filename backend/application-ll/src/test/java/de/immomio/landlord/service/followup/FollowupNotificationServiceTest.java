package de.immomio.landlord.service.followup;

import de.immomio.data.landlord.entity.property.followup.Followup;
import de.immomio.data.landlord.entity.property.followup.FollowupNotification;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupNotificationBean;
import de.immomio.model.repository.landlord.followup.FollowupNotificationRepository;
import de.immomio.service.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Fabian Beck
 */

public class FollowupNotificationServiceTest extends AbstractTest {

    @Mock
    private FollowupNotificationRepository notificationRepository;

    @Captor
    private ArgumentCaptor<List<FollowupNotification>> followupNotificationsCaptor;

    @InjectMocks
    private FollowupNotificationService followupNotificationService;

    private Date notificationDateOne;
    private Date notificationDateTwo;

    @Before
    public void init() {
        ZonedDateTime now = ZonedDateTime.now();

        notificationDateOne = Date.from(now.minusHours(1).toInstant());
        notificationDateTwo = Date.from(now.minusDays(1).toInstant());
    }

    @Test
    public void updateNotificationWithoutChanges(){
        Followup followup = new Followup();
        List<FollowupNotification> notifications = new ArrayList<>();

        notifications.add(createFollowupNotification(notificationDateOne));

        followup.setFollowupNotifications(notifications);

        List<FollowupNotificationBean> notificationBeans = new ArrayList<>();
        notificationBeans.add(FollowupNotificationBean.builder().date(notificationDateOne).build());

        followupNotificationService.updateNotifications(followup, notificationBeans);

        verify(notificationRepository).deleteAll(followupNotificationsCaptor.capture());
        Assert.assertTrue(followupNotificationsCaptor.getValue().isEmpty());

        verify(notificationRepository, never()).save(any(FollowupNotification.class));
    }

    @Test
    public void updateNotificationsWhereTwoAreAdded(){
        Followup followup = new Followup();
        followup.setFollowupNotifications(new ArrayList<>());

        List<FollowupNotificationBean> notificationBeans = new ArrayList<>();
        notificationBeans.add(FollowupNotificationBean.builder().date(notificationDateOne).build());
        notificationBeans.add(FollowupNotificationBean.builder().date(notificationDateTwo).build());

        followupNotificationService.updateNotifications(followup, notificationBeans);

        verify(notificationRepository).deleteAll(followupNotificationsCaptor.capture());
        Assert.assertTrue(followupNotificationsCaptor.getValue().isEmpty());

        ArgumentCaptor<FollowupNotification> followupNotificationArgumentCaptor = ArgumentCaptor.forClass(FollowupNotification.class);
        verify(notificationRepository, times(2)).save(followupNotificationArgumentCaptor.capture());
        Assert.assertEquals(notificationDateOne, followupNotificationArgumentCaptor.getAllValues().get(0).getDate());
        Assert.assertEquals(notificationDateTwo, followupNotificationArgumentCaptor.getAllValues().get(1).getDate());
    }

    @Test
    public void updateNotificationsWhereOneIsDeleted(){
        Followup followup = new Followup();

        List<FollowupNotification> notifications = new ArrayList<>();
        notifications.add(createFollowupNotification(notificationDateOne));

        followup.setFollowupNotifications(notifications);

        List<FollowupNotificationBean> notificationBeans = new ArrayList<>();

        followupNotificationService.updateNotifications(followup, notificationBeans);

        verify(notificationRepository).deleteAll(followupNotificationsCaptor.capture());
        List<FollowupNotification> deletedFollowupNotifications = followupNotificationsCaptor.getValue();
        Assert.assertEquals(1, deletedFollowupNotifications.size());
        Assert.assertEquals(notificationDateOne.getTime(), deletedFollowupNotifications.get(0).getDate().getTime());

        verify(notificationRepository, never()).save(any(FollowupNotification.class));
    }

    FollowupNotification createFollowupNotification(Date date) {
        FollowupNotification followupNotification = new FollowupNotification();
        followupNotification.setDate(date);
        followupNotification.setSent(false);
        return followupNotification;
    }
}
