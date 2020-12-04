package de.immomio.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ChangePasswordBean {

    @NotBlank
    private String password;

    @NotBlank
    private String confirmedPassword;

    public ChangePasswordBean() {
    }

    public ChangePasswordBean(String password, String confirmedPassword) {
        this();
        this.password = password;
        this.confirmedPassword = confirmedPassword;
    }

    public boolean checkNewPasswordConfirmed() {
        if (password == null || confirmedPassword == null) {
            return false;
        }

        return password.equals(confirmedPassword);
    }
}
