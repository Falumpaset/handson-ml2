package de.immomio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "custom.expose-pdf")
public class CustomExposePdfProperties {

    @Getter
    @Setter
    private Map<Long, String> customers;
}
