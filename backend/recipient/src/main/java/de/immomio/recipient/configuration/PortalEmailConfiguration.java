package de.immomio.recipient.configuration;

import de.immomio.recipient.handler.PortalEmailHandler;
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
@ConfigurationProperties(prefix = "imap.handlers.portal")
public class PortalEmailConfiguration extends EmailConfiguration {

    @Autowired
    protected PortalEmailConfiguration(PortalEmailHandler handler) {
        super(handler);
    }

    @Bean(name = "portalEmailProperty")
    public Properties emailProperties() {
        return createEmailProperties();
    }

    @Bean(name = "portalEmailReceiver")
    public ImapMailReceiver emailReceiver(@Qualifier("portalEmailProperty") Properties properties) {
        return createEmailReceiver(properties);
    }

    @Bean(name = "portalEmailFlow")
    public IntegrationFlow routeFlow(@Qualifier("portalEmailReceiver") ImapMailReceiver mailReceiver) {
        return createEmailFlow(mailReceiver);
    }
}
