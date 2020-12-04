package de.immomio.schufa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Johannes Hiemer.
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "schufa.prod")
public class SchufaProdConfig {

    // @Value("${schufa.host}")
    private String host;

    // @Value("${schufa.port}")
    private Integer port;

    @Value("${schufa.prod.accountcheck.credentials.participantnumber}")
    private String accountCheckParticipantNumber;

    @Value("${schufa.prod.accountcheck.credentials.participantpassword}")
    private String accountCheckParticipantPassword;

}
