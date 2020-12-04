package de.immomio.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class ImpersonateEmailBean {

    @NotBlank
    private String email;

    public ImpersonateEmailBean(String email) {
        this.email = email;
    }
}
