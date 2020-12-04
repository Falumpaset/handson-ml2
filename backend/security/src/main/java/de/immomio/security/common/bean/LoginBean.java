package de.immomio.security.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Johannes Hiemer
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginBean {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    public LoginBean() {
        super();
    }

    public LoginBean(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
