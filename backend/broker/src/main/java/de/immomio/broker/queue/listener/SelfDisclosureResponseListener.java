package de.immomio.broker.queue.listener;

import de.immomio.broker.service.SelfDisclosureService;
import de.immomio.messaging.container.selfdisclosure.SelfDisclosureResponseContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SelfDisclosureResponseListener implements MessageListener {

    private final BaseJsonConverter baseJsonConverter;

    private final SelfDisclosureService selfDisclosureService;

    @Autowired
    public SelfDisclosureResponseListener(
            SelfDisclosureService selfDisclosureService,
            BaseJsonConverter baseJsonConverter
    ) {
        this.selfDisclosureService = selfDisclosureService;
        this.baseJsonConverter = baseJsonConverter;
    }

    @Override
    public void onMessage(Message message) {
        SelfDisclosureResponseContainer container =
                (SelfDisclosureResponseContainer) baseJsonConverter.fromMessage(message);

        selfDisclosureService.sendResponseFeedbackEmail(container.getId(), container.getFeedbackEmail());
    }
}
