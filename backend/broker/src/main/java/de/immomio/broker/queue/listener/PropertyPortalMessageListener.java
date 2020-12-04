package de.immomio.broker.queue.listener;

import de.immomio.broker.service.property.portal.PropertyPortalMessageService;
import de.immomio.messaging.container.PropertyBrokerContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Maik Kingma.
 */
@Component
public class PropertyPortalMessageListener implements MessageListener {

    @Autowired
    private BaseJsonConverter baseJsonConverter;

    @Autowired
    private PropertyPortalMessageService propertyMessageService;

    /* (non-Javadoc)
     * @see org.springframework.amqp.core.MessageListener#onMessage(org.springframework.amqp.core.Message)
     */
    @Override
    public void onMessage(Message message) {
        PropertyBrokerContainer propertyBrokerContainer = (PropertyBrokerContainer) baseJsonConverter.fromMessage(
                message);

        propertyMessageService.onMessage(propertyBrokerContainer);
    }

}
