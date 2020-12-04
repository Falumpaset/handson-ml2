/**
 *
 */
package de.immomio.handler.ftpAccess;

import de.immomio.data.landlord.entity.ftpaccess.FtpAccess;
import de.immomio.messages.InternalCommunicationMessage;
import de.immomio.messaging.config.QueueConfigUtils;
import de.immomio.utilities.password.PasswordGenerator;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */

@Component
@RepositoryEventHandler()
public class FtpAccessEventHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @HandleBeforeCreate
    public void handleBeforeCreate(FtpAccess ftpAccess) {
        PasswordGenerator passwordGenerator = new PasswordGenerator(6);
        ftpAccess.setUserPassword(passwordGenerator.nextString());
        ftpAccess.setHomeDirectory("/" + ftpAccess.getCustomer().getId());

        this.refresh();
    }

    private void refresh() {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(QueueConfigUtils.ImporterConfig.EXCHANGE_NAME,
                "*." + QueueConfigUtils.ImporterConfig.IMPORTER_ROUTING_KEY,
                new InternalCommunicationMessage(
                        "application.refresh",
                        "task.init",
                        "web",
                        "*.importer"));
    }
}
