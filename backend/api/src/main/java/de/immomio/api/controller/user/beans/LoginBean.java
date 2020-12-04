package de.immomio.api.controller.user.beans;

import de.immomio.security.openid.LoginMethod;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public class LoginBean {

    @Email
    private String email;

    @NotBlank
    private String redirectUri;

    private String prompt;

    @NotBlank
    private String nonce;

    @NotBlank
    private String state;

    @NotNull
    private LoginMethod loginMethod;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LoginMethod getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(LoginMethod loginMethod) {
        this.loginMethod = loginMethod;
    }
}
