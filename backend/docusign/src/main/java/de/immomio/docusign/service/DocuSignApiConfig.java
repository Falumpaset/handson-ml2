package de.immomio.docusign.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andreas Hansen.
 */
@Configuration
@ConfigurationProperties("docusign")
@Getter
@Setter
public class DocuSignApiConfig {

    private String clientId;

    private String accountId;

    private String targetAccountId;

    private String authServer;

    private String privateKeyLocation;

    private long tokenExpirationInSeconds;

    private long tokenReplacementInSeconds;

    private String permissionScopes;

    private String oauthRedirectUri;

}
