package de.immomio.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordBean {

    @Email
    private String email;

    public ResetPasswordBean(String email) {
        this.email = email;
    }
}
