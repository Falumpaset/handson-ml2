package de.immomio.api.controller.user.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * @author Bastian Bliemeister
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ResetPasswordBean {

    @Email
    private String email;

}
