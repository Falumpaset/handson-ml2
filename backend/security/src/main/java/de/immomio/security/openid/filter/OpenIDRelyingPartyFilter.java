/**
 *
 */
package de.immomio.security.openid.filter;

import de.immomio.security.openid.token.OpenIDRelyingPartyToken;
import de.immomio.security.openid.utils.OpenIDFilterUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Johannes Hiemer.
 */
public class OpenIDRelyingPartyFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String X_AUTH_TOKEN = "X-Auth-Token";

    private AuthenticationManager authenticationManager;

    private RequestMatcher requiresAuthenticationRequestMatcher;

    private OpenIDFilterUtils openIDFilterUtils = new OpenIDFilterUtils();

    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    private boolean allowParameterToken;

    public OpenIDRelyingPartyFilter(AuthenticationManager authenticationManager, boolean allowParameterToken) {
        setFilterProcessesUrl("/**");
        this.setAuthenticationManager(authenticationManager);

        this.allowParameterToken = allowParameterToken;
    }

    public AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    public AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    private void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
                filterProcessesUrl));
    }

    public final void setRequiresAuthenticationRequestMatcher(
            RequestMatcher requestMatcher) {
        Assert.notNull(requestMatcher, "requestMatcher cannot be null");
        this.requiresAuthenticationRequestMatcher = requestMatcher;
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws
            IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request, response) || request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            chain.doFilter(request, response);

            return;
        }

        String token = getToken(request, response);
        if (!openIDFilterUtils.validateToken(token)) {
            unsuccessfulAuthentication(request, response, chain,
                    new InternalAuthenticationServiceException("No authorization header present."));

            return;
        }

        Authentication authResult;
        try {
            String plainToken = openIDFilterUtils.resolvePlainToken(token);
            authResult = this.authenticationManager.authenticate(new OpenIDRelyingPartyToken(plainToken));
            if (authResult == null) {
                return;
            }
        } catch (InternalAuthenticationServiceException failed) {
            logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, chain, failed);

            return;
        } catch (AuthenticationException failed) {
            unsuccessfulAuthentication(request, response, chain, failed);

            return;
        }

        successfulAuthentication(request, response, chain, authResult);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, FilterChain chain,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString());
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + failureHandler);
        }

        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                    + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        successHandler.onAuthenticationSuccess(request, response, authResult);

        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request, HttpServletResponse response) {
        String token = openIDFilterUtils.resolveToken(request, response, AUTHORIZATION_HEADER);
        if (token == null && allowParameterToken) {
            token = openIDFilterUtils.resolveTokenFromParameter(request, X_AUTH_TOKEN);
        }

        return token;
    }
}
