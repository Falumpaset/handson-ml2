package de.immomio.security.common.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractToken implements Serializable {
    private static final long serialVersionUID = -7372723596986491895L;

    private Long created;

    private Long expired;

    AbstractToken(Long created) {
        this.created = created;
    }
}
