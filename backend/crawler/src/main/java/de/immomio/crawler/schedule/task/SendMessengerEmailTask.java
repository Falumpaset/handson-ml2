package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.service.conversation.ConversationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class SendMessengerEmailTask extends BaseTask {

    private final ConversationEmailService conversationEmailService;

    @Autowired
    public SendMessengerEmailTask(ConversationEmailService conversationEmailService) {
        this.conversationEmailService = conversationEmailService;
    }

    @Override
    public boolean run()  {
        conversationEmailService.sendNewMessageEmails();
        return true;
    }
}
