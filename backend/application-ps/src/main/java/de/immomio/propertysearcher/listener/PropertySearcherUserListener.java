package de.immomio.propertysearcher.listener;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.service.sender.PropertySearcherMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler(PropertySearcherUserProfile.class)
public class PropertySearcherUserListener {

    private final PropertySearcherMessageSender messageSender;

    @Autowired
    public PropertySearcherUserListener(PropertySearcherMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @HandleAfterSave
    public void updateScoresAfterSave(PropertySearcherUserProfile userProfile) {
        log.info("disabled Update scores after userProfile changed. User Profile id = {}", userProfile.getId());
        messageSender.sendUserUpdateMessage(new PSUserProfileMessageContainer(userProfile.getId()));
    }

}
