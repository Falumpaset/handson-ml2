package de.immomio.itp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andreas Hansen
 **/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "itp.rest")
public class ItpWebClientConfiguration {

    private String host;

    private String token;

    private String basepath;

    private String mockbasepath;

}