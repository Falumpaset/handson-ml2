package de.immomio.recipient.configuration;

import de.immomio.recipient.handler.MessageHandler;
import de.immomio.recipient.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.support.PropertiesBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Slf4j
@Getter
@Setter
public abstract class EmailConfiguration {
    public static final String INBOX_BASE_URI = "imaps://%s:%s@imap.gmail.com:993/INBOX";

    private final MessageHandler messageHandler;

    private String email;

    private String password;

    private String protocol;

    private String debug;

    private String delete;

    @Value("${imap.usePoller}")
    private boolean usePoller;

    @Value("${imap.pollRate}")
    private Long pollRate;

    @Value("${imap.pollSize}")
    private Long pollSize;

    protected EmailConfiguration(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    protected Properties createEmailProperties() {
        PropertiesBuilder builder = new PropertiesBuilder();
        builder.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        builder.put("mail.imap.socketFactory.fallback", "false");
        builder.put("mail.imap.starttls.enable", "true");
        builder.put("mail.store.protocol", protocol);
        builder.put("mail.debug", debug);
        builder.put("mail.smtp.timeout", "10000");
        return builder.get();
    }

    protected ImapMailReceiver createEmailReceiver(Properties properties) {
        try {
            ImapMailReceiver mailReceiver = new ImapMailReceiver(getUri());
            mailReceiver.setShouldDeleteMessages(Boolean.parseBoolean(delete));
            mailReceiver.setJavaMailProperties(properties);
            mailReceiver.setSimpleContent(true);
            mailReceiver.afterPropertiesSet();
            return mailReceiver;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected IntegrationFlow createEmailFlow(ImapMailReceiver mailReceiver) {
        return createBaseFlowBuilder(mailReceiver).handle(this.messageHandler::handleMessage).get();
    }

    private String getUri() throws UnsupportedEncodingException {
        return String.format(INBOX_BASE_URI, StringUtils.encodeValue(email), password);
    }

    private IntegrationFlowBuilder createBaseFlowBuilder(ImapMailReceiver mailReceiver) {
        if (usePoller) {
            return IntegrationFlows.from(Mail.imapInboundAdapter(mailReceiver), e -> e.poller(Pollers.fixedRate(pollRate).maxMessagesPerPoll(pollSize)));
        }
        return IntegrationFlows.from(
                Mail.imapIdleAdapter(mailReceiver).autoStartup(true).shouldReconnectAutomatically(true));
    }
}
