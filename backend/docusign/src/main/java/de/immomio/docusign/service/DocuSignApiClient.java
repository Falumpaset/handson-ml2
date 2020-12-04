package de.immomio.docusign.service;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import de.immomio.docusign.service.beans.DocuSignApiAccountBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.docusign.esign.client.auth.OAuth.OAuthToken;

@Slf4j
@Component
public class DocuSignApiClient {

    private static final String API_CLIENT_BASE_PATH = "/restapi";
    private static final String ERROR_CONSENT_URL_FORMAT = "%s/oauth/auth?response_type=code&scope=%s" +
            "&client_id=%s" +
            "&redirect_uri=%s";
    private static final long SECOND_IN_MILLIS = 1000L;

    private final DocuSignApiConfig docuSignApiConfig;

    private byte[] privateKey;
    private final Map<UUID, DocuSignApiAccountBean> apiAccounts = new HashMap<>();

    @Autowired
    public DocuSignApiClient(DocuSignApiConfig docuSignApiConfig) {
        this.docuSignApiConfig = docuSignApiConfig;
    }

    @PostConstruct
    private void postConstruct() {
        try {
            this.privateKey = IOUtils.toByteArray(
                    getClass().getResourceAsStream(docuSignApiConfig.getPrivateKeyLocation()));
        } catch (IOException e) {
            log.error("Can't read private key for DocuSign - digital contract module not available.", e);
        }
    }

    public ApiClient getApiClient(UUID apiUserId) {
        ApiClient apiClient = new ApiClient();

        try {
            DocuSignApiAccountBean apiAccount = getApiAccount(apiUserId);
            setOrUpdateToken(apiClient, apiAccount, apiUserId);
            setOrUpdateAccountBasePath(apiClient, apiAccount);
        } catch (IOException e) {
            log.error("Can't get api client for DocuSign", e);
        } catch (ApiException e) {
            log.error("DocuSign Exception!");

            // Special handling for consent_required
            String message = e.getMessage();
            if (message != null && message.contains("consent_required")) {
                String consentUrl = String.format(ERROR_CONSENT_URL_FORMAT,
                        docuSignApiConfig.getAuthServer(),
                        docuSignApiConfig.getPermissionScopes(),
                        docuSignApiConfig.getClientId(),
                        docuSignApiConfig.getOauthRedirectUri());
                log.error("\nC O N S E N T   R E Q U I R E D" +
                        "\nAsk the user who will be impersonated to run the following url: " +
                        "\n" + consentUrl +
                        "\n\nIt will ask the user to login and to approve access by your application." +
                        "\nAlternatively, an Administrator can use Organization Administration to" +
                        "\npre-approve one or more users.");
            } else {
                log.error(String.format("    Reason: %d", e.getCode()));
                log.error(String.format("    Error Reponse: %s", e.getResponseBody()));
                log.error("DocuSign ApiClient error.", e);
            }
        }

        return apiClient;
    }

    public String getAccountId() {
        return docuSignApiConfig.getAccountId();
    }

    private void setOrUpdateToken(
            ApiClient apiClient,
            DocuSignApiAccountBean apiAccount,
            UUID apiUserId
    ) throws IOException, ApiException {
        long expiresIn = apiAccount.getExpiresIn();
        boolean isExpired = (System.currentTimeMillis() + docuSignApiConfig.getTokenReplacementInSeconds()) > expiresIn;
        log.info("Calling setOrUpdateToken, token={}, isExpired={}", apiAccount.getOAuthToken(), isExpired);
        OAuthToken oAuthToken = apiAccount.getOAuthToken();
        if (oAuthToken == null
                || (System.currentTimeMillis() + docuSignApiConfig.getTokenReplacementInSeconds()) > expiresIn) {
            oAuthToken = updateToken(apiClient, apiUserId.toString());
            expiresIn = System.currentTimeMillis() + (oAuthToken.getExpiresIn() * SECOND_IN_MILLIS);
            apiAccount.setOAuthToken(oAuthToken);
            apiAccount.setExpiresIn(expiresIn);
            apiAccount.setRefetchAccountBasePath(true);
        }
        apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());

        // set expiresIn for later check
        log.info("Finished setOrUpdateToken, token={}, expiresIn={}", oAuthToken, expiresIn);
    }

    private OAuth.OAuthToken updateToken(ApiClient apiClient, String userId) throws IOException, ApiException {
        List<String> scopes = new ArrayList<>();

        // Only signature scope is needed. Impersonation scope is implied.
        scopes.add(OAuth.Scope_SIGNATURE);

        apiClient.setOAuthBasePath(docuSignApiConfig.getAuthServer());

        log.info("DocuSign request new jwt token.");

        return apiClient.requestJWTUserToken(
                docuSignApiConfig.getClientId(),
                userId,
                scopes,
                privateKey,
                docuSignApiConfig.getTokenExpirationInSeconds());
    }

    private DocuSignApiAccountBean getApiAccount(UUID apiUserId) {
        log.info("getApiAccount for apiUserId: {}, apiAccounts: {}", apiUserId, apiAccounts);
        if (apiAccounts.containsKey(apiUserId)) {
            return apiAccounts.get(apiUserId);
        }
        DocuSignApiAccountBean apiAccount = new DocuSignApiAccountBean();
        apiAccounts.put(apiUserId, apiAccount);

        return apiAccount;
    }

    private void setOrUpdateAccountBasePath(
            ApiClient apiClient,
            DocuSignApiAccountBean apiAccount
    ) throws ApiException {
        if (apiAccount.isRefetchAccountBasePath()) {
            log.info("DocuSign request apiClient.getUserInfo.");
            OAuth.UserInfo userInfo = apiClient.getUserInfo(apiClient.getAccessToken());

            List<OAuth.Account> accounts = userInfo.getAccounts();
            OAuth.Account account = accounts
                    .stream()
                    .filter(acc -> Boolean.parseBoolean(acc.getIsDefault()))
                    .findFirst()
                    .orElseThrow();
            apiAccount.setAccountBasePath(account.getBaseUri() + API_CLIENT_BASE_PATH);
            apiAccount.setRefetchAccountBasePath(false);
        }
        apiClient.setBasePath(apiAccount.getAccountBasePath());
    }

}
