/**
 *
 */
package de.immomio.security.constants;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Johannes Hiemer.
 */
public class DefaultCorsHeader {

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}
