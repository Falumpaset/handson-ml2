/**
 *
 */
package de.immomio.security.service.helperClasses;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Johannes Hiemer.
 */

@Getter
@Setter
public class AuthenticationBean {

    private String name;

    private String token;

    private Long id;

    public AuthenticationBean(String name, String token, Long id) {
        super();
        this.name = name;
        this.token = token;
        this.id = id;
    }
}
