package de.immomio.security.common.bean;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class ApplicationIntentToken extends AbstractToken {

    private static final long serialVersionUID = -2139398223931303980L;

    private Long applicationId;

    public ApplicationIntentToken(Long applicationId) {
        super(DateTime.now().getMillis());
        this.applicationId = applicationId;
    }

    public ApplicationIntentToken() {
        super(DateTime.now().getMillis());
    }
}
