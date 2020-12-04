package de.immomio.security.common.bean;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class BrandingUrlToken extends AbstractToken {

    private static final long serialVersionUID = -2139398223931303980L;

    private Long customerId;

    public BrandingUrlToken(Long customerId) {
        super(DateTime.now().getMillis());

        this.customerId = customerId;
    }

    public BrandingUrlToken() {
        super(DateTime.now().getMillis());
    }
}
