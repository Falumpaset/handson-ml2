/**
 *
 */
package de.immomio.security.service.helperClasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Johannes Hiemer
 */

@Getter
@Setter
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
}
