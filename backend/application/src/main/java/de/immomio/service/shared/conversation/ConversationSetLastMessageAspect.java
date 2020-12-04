package de.immomio.service.shared.conversation;

import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.data.shared.entity.conversation.ConversationMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Aspect
@Component
public class ConversationSetLastMessageAspect {


    @Before("execution(* de.immomio.model.repository.core.shared.conversation.BaseConversationMessageRepository.save*(..))")
    public void setLastMessage(JoinPoint joinpoint) {
        ConversationMessage conversationMessage = (ConversationMessage) joinpoint.getArgs()[0];
        Conversation conversation = conversationMessage.getConversation();
        conversation.setLastMessageDate(new Date());
        conversation.setLastMessageSender(conversationMessage.getSender());
        conversation.setLastMessageText(conversationMessage.getMessage());
    }
}
