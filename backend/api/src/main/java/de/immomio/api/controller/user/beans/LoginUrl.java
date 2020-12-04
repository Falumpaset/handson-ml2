package de.immomio.api.controller.user.beans;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Johannes Hiemer.
 */
@Getter
@Setter
public class LoginUrl {

    private String host;

    private int port;

    private String scheme;

    private String path;

    private String clientId;

    private String redirectUri;

    private String nonce;

    private String state;

    private String responseType;

    private String responseMode;

    private String scope;

    private String kcIdpHint;

    private String loginHint;

    private String prompt;

    public LoginUrl(String scheme, String host, int port, String path, String clientId, String redirectUri,
                    String responseType, String responseMode, String scope, String nonce, String state,
                    String kcIdpHint,
                    String prompt) {
        super();
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.nonce = nonce;
        this.state = state;
        this.responseType = responseType;
        this.responseMode = responseMode;
        this.scope = scope;
        this.kcIdpHint = kcIdpHint;
        this.prompt = prompt;
    }

    public String getRedirectUrl() throws UnsupportedEncodingException, URISyntaxException {
        URI redirectURI = new URI(this.redirectUri);

        URIBuilder builder = new URIBuilder()
                .setHost(host)
                .setScheme(scheme)
                .setPort(port)
                .setPath(path)
                .addParameter("client_id", this.clientId)
                .addParameter("redirect_uri", redirectURI.toString())
                .addParameter("nonce", this.nonce)
                .addParameter("state", this.state)
                .addParameter("response_type", this.responseType)
                .addParameter("response_mode", this.responseMode)
                .addParameter("scope", this.scope)
                .addParameter("kc_idp_hint", this.kcIdpHint);

        if (this.loginHint != null && !this.loginHint.isEmpty()) {
            builder.addParameter("login_hint", this.loginHint);
        }

        if (this.prompt != null && !this.prompt.isEmpty()) {
            builder.addParameter("prompt", prompt);
        }

        URI uri = new URI(builder.toString());

        return uri.toString();
    }

}
