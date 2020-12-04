package de.immomio.api.controller.user.beans;

import javax.validation.constraints.NotBlank;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public class LogoutBean {

    @NotBlank
    private String redirectUri;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

}