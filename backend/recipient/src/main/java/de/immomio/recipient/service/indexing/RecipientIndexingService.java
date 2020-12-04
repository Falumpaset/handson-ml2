package de.immomio.recipient.service.indexing;

import de.immomio.reporting.model.event.AbstractEvent;
import de.immomio.reporting.model.event.EmailRecipientEvent;
import de.immomio.reporting.model.messaging.CustomerIndexingEvent;
import de.immomio.reporting.model.messaging.IndexingCustomerMessageContainer;
import de.immomio.reporting.model.messaging.IndexingEvent;
import de.immomio.reporting.model.messaging.IndexingMessageContainer;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import de.immomio.reporting.service.sender.AbstractElasticsearchIndexingService;
import lombok.extern.slf4j.Slf4j;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fabian Beck
 */

@Slf4j
@Service
public class RecipientIndexingService extends AbstractElasticsearchIndexingService {

    public static final String INDEX_UNPARSABLE_MAILS = "unparsable-mails";

    public RecipientIndexingService(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Async
    public void proposalAccepted(Email email) {
        List<AbstractEvent> events = new ArrayList<>();
        events.add(createEmailRecipientEvent(email));

        processIndexing(events, INDEX_UNPARSABLE_MAILS);
    }

    private EmailRecipientEvent createEmailRecipientEvent(Email email) {
        EmailRecipientEvent event = new EmailRecipientEvent();
        event.setSender(email.getFromRecipient() != null ? email.getFromRecipient().getAddress() : null);
        event.setRecipients(email.getRecipients().stream().map(Recipient::getAddress).collect(Collectors.toList()));
        event.setSubject(email.getSubject());
        event.setSentDate(email.getSentDate());
        event.setPlainContent(email.getPlainText());
        event.setHtmlContent(email.getHTMLText());
        return event;
    }
}
