package de.immomio.docusign.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @author Andreas Hansen.
 */
@Configuration
@ConfigurationProperties("docusign")
@Getter
@Setter
public class DocuSignUserConfig {

    private UUID adminUserId;

    private String permissionProfileId;

}
