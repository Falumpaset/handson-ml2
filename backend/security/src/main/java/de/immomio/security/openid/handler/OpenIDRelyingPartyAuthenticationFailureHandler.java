/**
 *
 */
package de.immomio.security.openid.handler;

import de.immomio.security.constants.DefaultCorsHeader;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Johannes Hiemer.
 */
public class OpenIDRelyingPartyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        response.addHeader(DefaultCorsHeader.ACCESS_CONTROL_EXPOSE_HEADERS,
                "WWW-Authenticate, Access-Control-Allow-Origin");
        response.addHeader(DefaultCorsHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.addHeader(DefaultCorsHeader.WWW_AUTHENTICATE, "Immomio realm="
                + DefaultCorsHeader.getBaseUrl(request)
                + "/users/auth/login");

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
    }

}
