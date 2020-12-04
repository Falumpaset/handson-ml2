package de.immomio.security.common.bean;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class UserEmailToken extends AbstractToken {

    private static final long serialVersionUID = -2139398223931303980L;

    private String email;

    private Long id;

    public UserEmailToken(Long id, String email) {
        super(DateTime.now().getMillis());
        this.id = id;
        this.email = email;
    }

    public UserEmailToken() {
        super(DateTime.now().getMillis());
    }
}
