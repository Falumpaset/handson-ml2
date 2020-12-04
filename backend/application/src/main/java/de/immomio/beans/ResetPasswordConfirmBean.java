package de.immomio.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Bastian Bliemeister
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ResetPasswordConfirmBean extends ChangePasswordBean {

    @NotBlank
    private String token;

}
