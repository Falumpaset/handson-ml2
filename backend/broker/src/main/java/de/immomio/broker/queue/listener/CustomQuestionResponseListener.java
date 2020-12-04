package de.immomio.broker.queue.listener;

import de.immomio.broker.service.application.PropertyApplicationService;
import de.immomio.broker.service.proposal.PropertyProposalService;
import de.immomio.messaging.container.customQuestion.CustomQuestionResponseContainer;
import de.immomio.messaging.converter.BaseJsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Niklas Lindemann
 */

@Service
@Slf4j
public class CustomQuestionResponseListener implements MessageListener {

    private final PropertyApplicationService propertyApplicationService;
    private final PropertyProposalService propertyProposalService;

    private final BaseJsonConverter baseJsonConverter;

    @Autowired
    public CustomQuestionResponseListener(
            PropertyApplicationService propertyApplicationService,
            PropertyProposalService propertyProposalService, BaseJsonConverter baseJsonConverter
    ) {
        this.propertyApplicationService = propertyApplicationService;
        this.propertyProposalService = propertyProposalService;
        this.baseJsonConverter = baseJsonConverter;
    }

    @Override
    public void onMessage(Message message) {
        CustomQuestionResponseContainer container =
                (CustomQuestionResponseContainer) baseJsonConverter.fromMessage(message);
        propertyApplicationService.setScoreForCustomQuestionResponse(container.getResponseId());
        propertyProposalService.setScoreForCustomQuestionResponse(container.getResponseId());
    }
}
