/**
 *
 */
package de.immomio.exporter.immoscout;

import de.immomio.data.base.type.credential.CredentialProperty;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Johannes Hiemer.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Immoscout24OAuth {

    private static final Logger log = LoggerFactory.getLogger(Immoscout24OAuth.class);

    private final String OAUTH_SECURITY_PREFIX = "/restapi/security/oauth/";

    @Value("${immoscout.endpoint}")
    private String ENDPOINT;

    @Value("${immoscout.consumer.key}")
    private String CONSUMER_KEY;

    @Value("${immoscout.secret.key}")
    private String CONSUMER_SECRET;

    private OAuthProvider provider;

    private OAuthConsumer consumer;

    private String getAuthorizationEndpoint() {
        return ENDPOINT + OAUTH_SECURITY_PREFIX + "confirm_access";
    }

    private String getAccessTokenEndpoint() {
        return ENDPOINT + OAUTH_SECURITY_PREFIX + "access_token";
    }

    private String getRequestTokenEndpoint() {
        return ENDPOINT + OAUTH_SECURITY_PREFIX + "request_token";
    }

    public String preAuth() throws OAuthMessageSignerException, OAuthNotAuthorizedException,
            OAuthExpectationFailedException, OAuthCommunicationException {
        consumer = new DefaultOAuthConsumer(CONSUMER_KEY,
                CONSUMER_SECRET);

        provider = new DefaultOAuthProvider(
                getRequestTokenEndpoint(), getAccessTokenEndpoint(),
                getAuthorizationEndpoint());

        log.info("Fetching request token from " + getRequestTokenEndpoint());
        final String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);

        log.info("Request token: " + consumer.getToken());
        log.info("Token secret: " + consumer.getTokenSecret());

        return authUrl;
    }

    public Map<String, String> postAuth(String pin) throws OAuthMessageSignerException, OAuthNotAuthorizedException,
            OAuthExpectationFailedException, OAuthCommunicationException {

        provider.retrieveAccessToken(consumer, pin);

        return CredentialProperty.initTokenProperties(consumer.getToken(), consumer.getTokenSecret());
    }

}
