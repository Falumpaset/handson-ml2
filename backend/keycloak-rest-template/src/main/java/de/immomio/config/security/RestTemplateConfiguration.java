package de.immomio.config.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Hiemer, Maik Kingma
 *
 */

@org.springframework.context.annotation.Configuration
@ConfigurationProperties("keycloak-rest-template")
public class RestTemplateConfiguration {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";
    private final String SECRET_KEY = "secret";

    @Value("${credentials.username}")
    private String username;

    @Value("${credentials.password}")
    private String password;

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

    @Bean
    public RestTemplate keycloakRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add((httpRequest, bytes, clientHttpRequestExecution) -> {
                    HttpHeaders headers = httpRequest.getHeaders();
                    if (!headers.containsKey(AUTH_HEADER)) {
                        AuthzClient client = AuthzClient.create(getConfiguration());
                        AccessTokenResponse authorization = client.obtainAccessToken(username, password);
                        headers.add(AUTH_HEADER, BEARER_TOKEN + authorization.getToken());
                    }
                    return clientHttpRequestExecution.execute(httpRequest, bytes);
                }
        );

        MappingJackson2HttpMessageConverter converter = getCustomConverter();
        restTemplate.getMessageConverters().add(0, converter);
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    private MappingJackson2HttpMessageConverter getCustomConverter() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("propertyFilter", SimpleBeanPropertyFilter.serializeAll());
        mapper.setFilterProvider(filterProvider);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }

    //TODO should use Refresh token if possible

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
        return new HashMap<String, Object>() {
            private static final long serialVersionUID = -6001088898815797727L;
            { put(SECRET_KEY, secret); }
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new Jackson2HalModule());

        return objectMapper;
    }
}
