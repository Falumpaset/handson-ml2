package de.immomio.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.common.ErrorCode;
import de.immomio.constants.exceptions.BadRequestException;
import de.immomio.constants.exceptions.UserNotFoundException;
import de.immomio.security.common.bean.ImpersonateResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 * @author Maik Kingma
 */

@Slf4j
@Service
public class KeycloakService {

    public static final String ENTITLED_GROUPS = "MC_ENTITLED_GROUPS";

    public static final String CUSTOMER_DOMAIN = "MC_CUSTOMER_DOMAIN";
    public static final String NO_USER_WITH_THAT_EMAIL_IN_SSO_L = "NO_USER_WITH_THAT_EMAIL_IN_SSO_L";

    @Value("${keycloak.endpoint}")
    private String endpoint;

    @Value("${keycloak.auth-realm}")
    private String authRealm;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.impersonate.client}")
    private String impersonateClient;

    @Value("${keycloak.impersonate.password}")
    private String impersonatePassword;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Keycloak keycloak;

    private static final String IMPERSONATE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";

    private static final String IMPERSONATE_REQUESTED_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:access_token";

    private static final String PARAM_CLIENT_ID = "client_id";

    private static final String PARAM_CLIENT_SECRET = "client_secret";

    private static final String PARAM_REQUESTED_SUBJECT = "requested_subject";

    private static final String PARAM_GRANT_TYPE = "grant_type";

    private static final String PARAM_REQUESTED_TOKEN_TYPE = "requested_token_type";

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                        .build(),
                NoopHostnameVerifier.INSTANCE);


        HttpRequestRetryHandler requestRetryHandler = (exception, executionCount, httpContext) -> {
            log.info("retry#" + executionCount);
            if ((exception instanceof ConnectTimeoutException ||
                    exception instanceof SocketTimeoutException) ||
                    exception instanceof NoHttpResponseException
                    && executionCount <= 5) {
                return true;
            }
            return false;
        };

        SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(5000)
                .build();

        HttpClient httpClient = HttpClientBuilder
                .create()
                .setRetryHandler(requestRetryHandler)
                .setDefaultSocketConfig(socketConfig)
                .setSSLSocketFactory(socketFactory)
                .build();
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);

        ResteasyClient resteasyClient = new ResteasyClientBuilder()
                .disableTrustManager()
                .connectionPoolSize(10)
                .httpEngine(engine)
                .build();

        keycloak = KeycloakBuilder.builder()
                .serverUrl(endpoint)
                .realm(authRealm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .resteasyClient(resteasyClient)
                .build();
    }

    /**
     * Creates a default User with the DOMAIN_ADMIN_GROUP and a mapping into the CUSTOMER_DOMAIN.
     *
     * @param organisationName
     * @param username
     * @param email
     * @param firstName
     * @param lastName
     * @return boolean
     */
    public boolean createUser(
            String organisationName,
            String username,
            String email,
            String firstName,
            String lastName
    ) {
        return createUser(organisationName, username, email, firstName, lastName, null, true);
    }

    public boolean createUser(
            String organisationName,
            String username,
            String email,
            String firstName,
            String lastName,
            boolean enabled
    ) {
        return createUser(organisationName, username, email, firstName, lastName, null, enabled);
    }

    public boolean createUser(
            String organisationName,
            String username,
            String email,
            String firstName,
            String lastName,
            String password
    ) {
        return createUser(organisationName, username, email, firstName, lastName, password, true);
    }

    /**
     * Creates a default User with the DOMAIN_ADMIN_GROUP and a mapping into the CUSTOMER_DOMAIN.
     *
     * @param organisationName
     * @param email
     * @param firstName
     * @param lastName
     * @param credentialPassword
     * @param enabled
     * @return boolean
     */
    public boolean createUser(
            String organisationName,
            String username,
            String email,
            String firstName,
            String lastName,
            String credentialPassword,
            boolean enabled
    ) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(enabled);

        Response response = keycloak.realm(realm)
                                    .users()
                                    .create(user);

        List<UserRepresentation> users = this.searchUser(user.getEmail());
        if (response.getStatus() == 201 && users.size() == 1) {
            user = users.get(0);

            if (user.getAttributes() == null) {
                user.setAttributes(new HashMap<>());
            }

            this.assignCustomerDomain(user, organisationName);
            this.updateUser(user);

            if (credentialPassword != null && !credentialPassword.isEmpty()) {
                keycloak.realm(realm)
                        .users()
                        .get(user.getId())
                        .resetPassword(createCredentials(credentialPassword, false));
            }

            log.info("User successfully created", response.getEntity());
            response.close();
            return true;
        }
        log.info("Could not create User", response.getEntity());
        response.close();
        return false;
    }

    public List<UserRepresentation> searchUser(String searchParams) {
        List<UserRepresentation> userRepresentations = keycloak.realm(realm)
                .users()
                .search(searchParams, 0, 999999);

        List<UserRepresentation> equalUsers = userRepresentations.stream()
                .filter(userRepresentation -> userRepresentation.getEmail().equalsIgnoreCase(searchParams))
                .collect(Collectors.toList());

        return equalUsers;
    }

    public boolean userExists(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        List<UserRepresentation> list = searchUser(email);

        return list.size() > 0;
    }

    public void updateUser(UserRepresentation user) {
        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .update(user);
    }

    public void changeEmail(UserRepresentation user, String newEmail) {
        user.setEmail(newEmail);
        user.setUsername(newEmail);
        updateUser(user);
    }

    public void removeUser(String email) {
        List<UserRepresentation> users = this.searchUser(email);

        if (users.size() != 1) {
            return;
        }

        removeUser(users.get(0));
    }

    public void removeUsersByEmail(List<String> emails) {
        emails.forEach(this::removeUser);
    }

    public void removeUser(UserRepresentation user) {
        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .remove();
    }

    public ImpersonateResponse impersonateUser(String email) {
        List<UserRepresentation> users = this.searchUser(email);

        if (users.size() != 1) {
            return null;
        }

        return sendImpersonateRequest(users.get(0));
    }

    public void resetPassword(String email, String password) {
        resetPassword(email, password, true);
    }

    public void resetPassword(String email, String password, boolean isTemporary) {
        List<UserRepresentation> users = this.searchUser(email);

        if (users.size() != 1) {
            return;
        }

        resetPassword(users.get(0), password, isTemporary);
    }

    public void resetPassword(UserRepresentation user, String password) {
        resetPassword(user, password, true);
    }

    public void resetPassword(UserRepresentation user, String password, boolean isTemporary) {
        CredentialRepresentation credentialRepresentation = this.createCredentials(password, isTemporary);

        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .resetPassword(credentialRepresentation);
    }

    public CredentialRepresentation createCredentials(String credentialPassword, boolean isTemporary) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(credentialPassword);
        credential.setTemporary(isTemporary);

        return credential;
    }

    public void assignCustomerDomain(UserRepresentation user, String customerName) {
        ArrayList<String> value = new ArrayList<>();
        value.add(customerName);

        user.getAttributes().put(CUSTOMER_DOMAIN, value);
    }

    public void updateEntitledGroups(UserRepresentation user, String groupName) {
        List<String> groups;
        try {
            if (user.getAttributes() == null) {
                user.setAttributes(new HashMap<>());
            }

            groups = this.decodeGroupAttribute(user.getAttributes());

            if (groups.indexOf(groupName) == -1) {
                groups.add(groupName);
            }

            List<String> encodedGroups = this.encodeGroupAttribute(groups);

            user.getAttributes().put(KeycloakService.ENTITLED_GROUPS, encodedGroups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImpersonateResponse sendImpersonateRequest(UserRepresentation userRepresentation) {
        try {
            RestTemplate restTemplate = initializeRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = createImpersonateRequest(userRepresentation.getId());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity response = restTemplate.postForEntity(getImpersonateRequestUrl(),
                                                                 request,
                                                                 ImpersonateResponse.class);

            return (ImpersonateResponse) response.getBody();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    private RestTemplate initializeRestTemplate() throws KeyStoreException, NoSuchAlgorithmException,
            KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, certificate) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                                                               .loadTrustMaterial(null, acceptingTrustStrategy)
                                                               .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }

    private String getImpersonateRequestUrl() {
        return String.format("%s/realms/%s/protocol/openid-connect/token", endpoint, realm);
    }

    private MultiValueMap<String, String> createImpersonateRequest(String impersonatedUserId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(PARAM_CLIENT_ID, impersonateClient);
        params.add(PARAM_CLIENT_SECRET, impersonatePassword);
        params.add(PARAM_REQUESTED_SUBJECT, impersonatedUserId);
        params.add(PARAM_GRANT_TYPE, IMPERSONATE_GRANT_TYPE);
        params.add(PARAM_REQUESTED_TOKEN_TYPE, IMPERSONATE_REQUESTED_TOKEN_TYPE);

        return params;
    }

    private List<String> decodeGroupAttribute(Map<String, List<String>> attributes)
            throws IOException {
        List<String> groupAttributes = new ArrayList<>();

        if (attributes.containsKey(ENTITLED_GROUPS)) {
            groupAttributes = attributes.get(ENTITLED_GROUPS);

            if (groupAttributes.size() == 1 && groupAttributes.get(0) != null && groupAttributes.get(0).length() > 0) {
                return objectMapper.readValue(groupAttributes.get(0), new TypeReference<List<String>>() {
                });
            }
        }

        return groupAttributes;
    }

    private List<String> encodeGroupAttribute(List<String> decodedGroups) throws JsonProcessingException {

        List<String> groups = new ArrayList<>();
        String groupString = objectMapper.writeValueAsString(decodedGroups);
        groups.add(groupString);

        return groups;
    }

    public void createIdentityProvider() {
        IdentityProviderRepresentation identityProviderRepresentation = new IdentityProviderRepresentation();

        keycloak.realm(realm)
                .identityProviders()
                .create(identityProviderRepresentation);
    }

    public void activateUser(String email) throws UserNotFoundException {
        List<UserRepresentation> users = this.searchUser(email);
        if (validateUsers(users)) {
            throw new UserNotFoundException(NO_USER_WITH_THAT_EMAIL_IN_SSO_L);
        }
        updateEnabled(users.get(0), true);
    }

    private boolean validateUsers(List<UserRepresentation> users) {
        return users == null || users.isEmpty() || users.size() > 1;
    }

    public void deactivateUser(String email) throws UserNotFoundException {
        List<UserRepresentation> users = this.searchUser(email);
        if (validateUsers(users)) {
            throw new UserNotFoundException(NO_USER_WITH_THAT_EMAIL_IN_SSO_L);
        }
        updateEnabled(users.get(0), false);
    }

    private void updateEnabled(UserRepresentation userRepresentation, boolean enabled) {
        userRepresentation.setEnabled(enabled);
        keycloak.realm(realm)
                    .users()
                    .get(userRepresentation.getId())
                    .update(userRepresentation);
    }

    public void registerInKeycloak(String organisationName, boolean isSocialLogin, String email, String firstName, String lastName, String password) {
        if (!isSocialLogin) {
            List<UserRepresentation> keyCloakUsers = searchUser(email);
            if (!keyCloakUsers.isEmpty()) {
                keyCloakUsers.forEach(keyCloakUser -> removeUser(keyCloakUser.getEmail()));
            }

            boolean created = createUser(organisationName, email, email, firstName, lastName, password);

            if (!created) {
                throw new BadRequestException(ErrorCode.USER_ALREADY_EXISTS_IN_KEYCLOAK);
            }
        }
    }
}
