/**
 *
 */
package de.immomio.security.openid.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Johannes Hiemer.
 */
public class OpenIDRelyingPartyToken extends AbstractAuthenticationToken {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String token;

    private Object principal;

    public OpenIDRelyingPartyToken(String token) {
        super(null);
        this.token = token;
    }

    public OpenIDRelyingPartyToken(Collection<? extends GrantedAuthority> authorities, Object principal) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public OpenIDRelyingPartyToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = userDetails;
        super.setAuthenticated(true);
    }

    public String getToken() {
        return token;
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        super.getAuthorities().addAll(authorities);
    }

}
