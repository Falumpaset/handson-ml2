/**
 *
 */
package de.immomio.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Getter
@Setter
@NoArgsConstructor
public class ChangeEmailBean {

    @NotBlank
    private String email;

    public ChangeEmailBean(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email != null ? email.toLowerCase() : null;
    }
}
