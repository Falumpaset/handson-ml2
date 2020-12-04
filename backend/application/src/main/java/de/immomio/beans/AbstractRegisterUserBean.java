package de.immomio.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class AbstractRegisterUserBean<T extends AbstractCustomerRegisterBean> {

    @NotBlank
    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    private String confirmPassword;

    @NotNull
    private T customer;

    public String getEmail() {
        return email.toLowerCase();
    }
}
