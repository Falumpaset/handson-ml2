package de.immomio.schufa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Johannes Hiemer.
 **/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "schufa.rest")
@ConditionalOnProperty(prefix = "schufa.rest", name = {"scheme", "host", "port", "username", "password"})
public class SchufaRestClientConfiguration {

    private String scheme;

    private String host;

    private int port;

    private String username;

    private String password;
}