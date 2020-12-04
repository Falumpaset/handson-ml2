package de.immomio.schufa;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Hiemer.
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "schufa")
public class SchufaConfig {

    // @Value("${schufa.host}")
    private String host;

    // @Value("${schufa.port}")
    private Integer port;

    @Value("${schufa.accountcheck.credentials.participantnumber}")
    private String accountCheckParticipantNumber;

    @Value("${schufa.accountcheck.credentials.participantpassword}")
    private String accountCheckParticipantPassword;

    // @Value("${schufa.testibans}")
    private List<String> testibans = new ArrayList<>();

}
