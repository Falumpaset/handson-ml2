package de.immomio.recipient.configuration;

import de.immomio.recipient.handler.ConversationMessageEmailHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.ImapMailReceiver;

import java.util.Properties;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "imap.handlers.conversation")
public class ConversationMessageEmailConfiguration extends EmailConfiguration {

    @Autowired
    protected ConversationMessageEmailConfiguration(ConversationMessageEmailHandler handler) {
        super(handler);
    }

    @Bean(name = "conversationMessageEmailProperty")
    public Properties emailProperties() {
        return createEmailProperties();
    }

    @Bean(name = "conversationMessageEmailReceiver")
    public ImapMailReceiver emailReceiver(@Qualifier("conversationMessageEmailProperty") Properties properties) {
        return createEmailReceiver(properties);
    }

    @Bean(name = "conversationMessageEmailFlow")
    public IntegrationFlow routeFlow(@Qualifier("conversationMessageEmailReceiver") ImapMailReceiver mailReceiver) {
        return createEmailFlow(mailReceiver);
    }
}
