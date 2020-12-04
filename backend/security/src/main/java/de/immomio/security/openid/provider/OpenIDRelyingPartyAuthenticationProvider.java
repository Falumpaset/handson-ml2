package de.immomio.security.openid.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.security.openid.token.OpenIDRelyingPartyToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 */
public class OpenIDRelyingPartyAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private static final String EXP = "exp";

    private static final String EMAIL = "email";

    private String publicKey = null;

    private ObjectMapper objectMapper = null;

    private UserDetailsService userDetailsService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.publicKey, "The publicKey must be set");
        Assert.notNull(this.objectMapper, "The objectMapper must be set");
        Assert.notNull(this.userDetailsService, "The userDetailsService must be set");
    }

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        if (authentication instanceof OpenIDRelyingPartyToken) {
            OpenIDRelyingPartyToken response = (OpenIDRelyingPartyToken) authentication;

            Jwt jwt = JwtHelper.decode(response.getToken());
            try {
                RsaVerifier rsaVerifier = new RsaVerifier(publicKey);
                jwt.verifySignature(rsaVerifier);
            } catch (Exception ex) {
                throw new AuthenticationServiceException("Error verifying signature of token");
            }

            if (jwt.getClaims() == null) {
                throw new AuthenticationServiceException("Error parsing JWT token/extracting claims");
            }

            Map<String, Object> claims;
            try {
                claims = objectMapper.readValue(jwt.getClaims(), new TypeReference<HashMap<String, Object>>() {
                });
            } catch (IOException e) {
                throw new AuthenticationServiceException("Error parsing claims from JTW");
            }

            if (claims != null && claims.size() > 0) {
                String username = (String) claims.get(EMAIL);
                Long timestamp = (long) ((Integer) claims.get(EXP)) * 1000;
                Date now = new Date();
                Date expirationTime = new Date(timestamp);

                if (now.before(expirationTime)) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userDetails != null) {
                        return createSuccessfulAuthentication(userDetails, response);
                    } else {
                        throw new AuthenticationServiceException("User could not be found in the database");
                    }
                } else {
                    throw new AuthenticationServiceException("Token expiration timed out");
                }
            } else {
                throw new AuthenticationServiceException("Error parsing claims from JTW");
            }

        }

        return null;
    }

    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, OpenIDRelyingPartyToken auth) {
        return new OpenIDRelyingPartyToken(userDetails,
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public boolean supports(Class<?> authentication) {
        return OpenIDRelyingPartyToken.class.isAssignableFrom(authentication);
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
