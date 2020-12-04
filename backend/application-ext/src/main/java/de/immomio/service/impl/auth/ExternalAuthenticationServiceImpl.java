package de.immomio.service.impl.auth;

import de.immomio.exception.ExternalApiUnauthorizedException;
import de.immomio.model.repository.landlord.customer.user.LandlordExternalApiUserRepository;
import de.immomio.service.base.auth.ExternalAuthenticationService;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Service
public class ExternalAuthenticationServiceImpl implements ExternalAuthenticationService {

    @Value("${keycloak.restTemplate.realm}")
    private String realm;

    @Value("${keycloak.restTemplate.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.restTemplate.ssl-required}")
    private String sslRequired;

    @Value("${keycloak.restTemplate.resource}")
    private String resource;

    @Value("${keycloak.restTemplate.public-client}")
    private boolean publicClient;

    @Value("${keycloak.restTemplate.use-resource-role-mappings}")
    private boolean useResourceRoleMappings;

    @Value("${keycloak.restTemplate.confidential-port}")
    private int confidentialPort;

    @Value("${keycloak.restTemplate.credentials.secret}")
    private String secret;

    private AuthzClient client;

    private final String SECRET_KEY = "secret";

    private final LandlordExternalApiUserRepository externalApiUserRepository;

    public ExternalAuthenticationServiceImpl(LandlordExternalApiUserRepository externalApiUserRepository) {
        this.externalApiUserRepository = externalApiUserRepository;
    }

    @PostConstruct
    public void init() {
        this.client = AuthzClient.create(getConfiguration());
    }

    public String getAuthToken(String username, String password) {
        externalApiUserRepository.findFirstByUsername(username.toLowerCase()).orElseThrow(ExternalApiUnauthorizedException::new);
        AccessTokenResponse authorization = client.obtainAccessToken(username, password);
        return authorization.getToken();
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setRealm(realm);
        configuration.setAuthServerUrl(authServerUrl);
        configuration.setSslRequired(sslRequired);
        configuration.setResource(resource);
        configuration.setPublicClient(publicClient);
        configuration.setUseResourceRoleMappings(useResourceRoleMappings);
        configuration.setConfidentialPort(confidentialPort);
        configuration.setCredentials(getCredentials());

        return configuration;
    }

    private Map<String, Object> getCredentials() {
        return new HashMap<>() {
            private static final long serialVersionUID = -6001088898815797727L;
            {
                put(SECRET_KEY, secret);
            }
        };
    }
}
