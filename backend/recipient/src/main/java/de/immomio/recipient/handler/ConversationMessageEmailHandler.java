package de.immomio.recipient.handler;

import de.immomio.beans.shared.conversation.ConversationMessageCreateBean;
import de.immomio.data.shared.entity.conversation.Conversation;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import de.immomio.model.repository.core.shared.conversation.BaseConversationRepository;
import de.immomio.recipient.service.conversation.ConversationConfigService;
import de.immomio.recipient.service.conversation.ConversationMessageNotificationService;
import de.immomio.recipient.service.conversation.ConversationMessageService;
import de.immomio.recipient.service.conversation.EmailAttachmentConverter;
import de.immomio.recipient.service.conversation.EmailBodyConverter;
import lombok.extern.slf4j.Slf4j;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.converter.EmailConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fabian Beck.
 */

@Slf4j
@Component
public class ConversationMessageEmailHandler implements MessageHandler {

    public static final String REGEX_START = "^";
    public static final String REGEX_AT = "@";
    public static final String REGEX_END = "$";
    public static final String REGEX_POINT = "\\.";
    public static final String REGEX_POINT_PART = "\\\\.";
    private static final String REGEX_PLUS_PART = "\\\\+";
    private static final String REGEX_EMAIL_ID_PART = "([a-z0-9-]+)";

    private final ConversationMessageService conversationMessageService;
    private final BaseConversationRepository baseConversationRepository;
    private final BasePropertySearcherUserRepository basePropertySearcherUserRepository;
    private final EmailAttachmentConverter emailAttachmentConverter;
    private final EmailBodyConverter emailBodyConverter;
    private final ConversationConfigService conversationConfigService;
    private final ConversationMessageNotificationService conversationMessageNotificationService;

    private final Pattern emailPattern;
    private final boolean checkPSEmail;

    @Autowired
    public ConversationMessageEmailHandler(ConversationMessageService conversationMessageService,
            BaseConversationRepository baseConversationRepository,
            BasePropertySearcherUserRepository basePropertySearcherUserRepository,
            EmailAttachmentConverter emailAttachmentConverter,
            EmailBodyConverter emailBodyConverter,
            ConversationConfigService conversationConfigService,
            ConversationMessageNotificationService conversationMessageNotificationService,
            @Value("${imap.handlers.conversation.email}") String email,
            @Value("${replyViaEmail.checkPSEmail}") boolean checkPSEmail) {
        this.conversationMessageService = conversationMessageService;
        this.baseConversationRepository = baseConversationRepository;
        this.basePropertySearcherUserRepository = basePropertySearcherUserRepository;
        this.emailAttachmentConverter = emailAttachmentConverter;
        this.emailBodyConverter = emailBodyConverter;
        this.conversationConfigService = conversationConfigService;
        this.conversationMessageNotificationService = conversationMessageNotificationService;
        this.emailPattern = Pattern.compile(getMailRegex(email));
        this.checkPSEmail = checkPSEmail;
    }

    @Override
    public void handleMessage(Message message) {
        try {
            Email email = EmailConverter.mimeMessageToEmail((MimeMessage) message.getPayload());

            email.getRecipients()
                    .stream()
                    .filter(this::isValidRecipient)
                    .findFirst()
                    .ifPresentOrElse((recipient) -> saveMessage(recipient, email),
                            () -> sendEmailWithoutIdEmail(email));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static String getMailRegex(String email) {
        return REGEX_START + email.replaceFirst(REGEX_AT, REGEX_PLUS_PART + REGEX_EMAIL_ID_PART + REGEX_AT)
                .replaceAll(REGEX_POINT, REGEX_POINT_PART) + REGEX_END;
    }

    private void saveMessage(Recipient recipient, Email email) {
        Optional.of(recipient)
                .map(this::extractExternalId)
                .map(baseConversationRepository::getFirstByExternalId)
                .filter((conversation) -> isResponsePossible(conversation, email))
                .ifPresent(conversation -> {
                    ConversationMessageCreateBean conversationMessageCreateBean = parseEmail(email);
                    conversationMessageService.addNewMessage(conversation, conversationMessageCreateBean);
                });
    }

    private boolean isResponsePossible(Conversation conversation, Email email) {
        if (!conversationConfigService.isPropertySearcherAbleToRespondViaMail(conversation.getApplication())) {
            return false;
        }

        if (!checkPSEmail) {
            return true;
        }

        return conversation.getApplication()
                .getUserProfile()
                .getEmail()
                .equals(email.getFromRecipient().getAddress());
    }

    private void sendEmailWithoutIdEmail(Email email) {
        Recipient sender = email.getFromRecipient();
        if (sender != null && basePropertySearcherUserRepository.existsByEmail(sender.getAddress())) {
            conversationMessageNotificationService.sendExternalIdNotAssignableEmail(sender.getAddress());
        }
    }

    private boolean isValidRecipient(Recipient recipient) {
        return recipient.getAddress().matches(emailPattern.pattern());
    }

    private String extractExternalId(Recipient recipient) {
        Matcher matcher = emailPattern.matcher(recipient.getAddress());

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private ConversationMessageCreateBean parseEmail(Email email) {
        ConversationMessageCreateBean conversationMessageCreateBean = new ConversationMessageCreateBean();
        conversationMessageCreateBean.setMessage(emailBodyConverter.extractMessage(email));
        conversationMessageCreateBean.setAttachments(emailAttachmentConverter.saveAttachments(email));
        return conversationMessageCreateBean;
    }
}
