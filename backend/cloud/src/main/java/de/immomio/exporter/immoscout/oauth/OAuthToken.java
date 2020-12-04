/**
 *
 */
package de.immomio.exporter.immoscout.oauth;

/**
 * @author Johannes Hiemer
 */
public class OAuthToken {

    private String requestToken;

    private String requestTokenSecret;

    public OAuthToken() {
    }

    public OAuthToken(String requestToken, String requestTokenSecret) {
        super();
        this.requestToken = requestToken;
        this.requestTokenSecret = requestTokenSecret;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getRequestTokenSecret() {
        return requestTokenSecret;
    }

    public void setRequestTokenSecret(String requestTokenSecret) {
        this.requestTokenSecret = requestTokenSecret;
    }

}
