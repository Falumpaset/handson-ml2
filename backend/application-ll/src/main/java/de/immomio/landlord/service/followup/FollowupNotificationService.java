package de.immomio.landlord.service.followup;

import de.immomio.data.landlord.entity.property.followup.Followup;
import de.immomio.data.landlord.entity.property.followup.FollowupNotification;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupNotificationBean;
import de.immomio.model.repository.landlord.followup.FollowupNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck
 */

@Service
public class FollowupNotificationService {

    private FollowupNotificationRepository notificationRepository;

    @Autowired
    public FollowupNotificationService(FollowupNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void updateNotifications(Followup followup, List<FollowupNotificationBean> notificationBeans) {
        List<FollowupNotification> notifications = followup.getFollowupNotifications();

        List<FollowupNotification> notificationsToDelete = notifications.stream()
                .filter(followupNotification -> notificationBeans.stream()
                        .noneMatch(followupNotificationBean -> followupNotificationBean.getDate()
                                .compareTo(followupNotification.getDate()) == 0))
                .collect(Collectors.toList());

        notificationRepository.deleteAll(notificationsToDelete);

        notificationBeans.stream()
                .filter(bean -> notifications.stream().noneMatch(notification -> notification.getDate()
                        .compareTo(bean.getDate()) == 0))
                .forEach(bean -> createNotification(followup, bean));
    }

    private void createNotification(Followup followup, FollowupNotificationBean bean) {
        FollowupNotification notification = new FollowupNotification();
        notification.setDate(bean.getDate());
        notification.setFollowup(followup);
        notification.setSent(false);
        notificationRepository.save(notification);
    }
}
