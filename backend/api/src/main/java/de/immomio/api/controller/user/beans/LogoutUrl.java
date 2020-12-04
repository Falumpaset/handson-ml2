package de.immomio.api.controller.user.beans;

import org.apache.http.client.utils.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Johannes Hiemer.
 */
public class LogoutUrl {

    private String host;

    private int port;

    private String scheme;

    private String path;

    private String redirectUri;

    public LogoutUrl(String scheme, String host, int port, String path, String redirectUri) {
        super();

        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.redirectUri = redirectUri;
    }

    public String getRedirectUrl() throws UnsupportedEncodingException, URISyntaxException {
        URI redirectURI = new URI(this.redirectUri);

        URIBuilder builder = new URIBuilder()
                .setHost(host)
                .setScheme(scheme)
                .setPort(port)
                .setPath(path)
                .addParameter("redirect_uri", redirectURI.toString());

        URI uri = new URI(builder.toString());

        return uri.toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

}