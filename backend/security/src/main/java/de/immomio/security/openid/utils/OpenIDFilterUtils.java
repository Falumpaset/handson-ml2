/**
 *
 */
package de.immomio.security.openid.utils;

import de.immomio.security.common.utils.CommonFilterUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Johannes Hiemer.
 */
public class OpenIDFilterUtils extends CommonFilterUtils {

    private static final String BEARER = "Bearer";

    public String resolvePlainToken(String input) {
        Assert.notNull(input, "Token might not be empty");

        return input.substring(BEARER.length()).trim();
    }

    @Override
    public String resolveToken(HttpServletRequest request, HttpServletResponse response, String tokenName) {
        Assert.notNull(tokenName, "tokenName might not be null/or empty");
        return request.getHeader(tokenName);
    }

    public String resolveTokenFromParameter(HttpServletRequest request, String tokenName) {
        String token = request.getParameter(tokenName);

        if (token != null && Base64.isBase64(token.getBytes())) {
            token = new String(Base64.decodeBase64(token));
        }

        return token;
    }

    public boolean validateToken(HttpServletRequest request, HttpServletResponse response, String tokenName) {
        Assert.notNull(tokenName, "tokenName might not be null/or empty");
        String token = request.getHeader(tokenName);

        return validateToken(token);
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        } else {
            if (!token.contains(BEARER)) {
                return false;
            }

            try {
                JwtHelper.decode(resolvePlainToken(token));
            } catch (Exception ex) {
                return false;
            }
        }
        return true;
    }

}
