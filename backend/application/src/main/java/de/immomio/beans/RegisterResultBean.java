package de.immomio.beans;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.ObjectError;

@Getter
@Setter
@EqualsAndHashCode
public class RegisterResultBean<T extends AbstractUser> {

    private T registeredUser;

    private ObjectError error;

    private String token;

    public boolean hasError() {
        return error != null;
    }
}
