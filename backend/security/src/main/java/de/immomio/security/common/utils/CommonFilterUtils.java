/**
 *
 */
package de.immomio.security.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.security.common.validation.ValidationUtils;
import de.immomio.security.service.helperClasses.LoginBean;
import de.immomio.security.validation.UsernamePasswordValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Johannes Hiemer.
 */
public class CommonFilterUtils {

    protected LoginBean loginBean;

    private ObjectMapper objectMapper = new ObjectMapper();

    public boolean verifyHeaderContentType(HttpServletRequest request) {
        return MediaType.valueOf("application/hal+json")
                .isCompatibleWith(MediaType.parseMediaType(request.getHeader("Content-Type"))) ||
                MediaType.APPLICATION_JSON
                        .isCompatibleWith(MediaType.parseMediaType(request.getHeader("Content-Type")));
    }

    public String obtainUsername(HttpServletRequest request) {
        String username = null;

        if (verifyHeaderContentType(request)) {
            username = loginBean.getUsername();
        }

        return username;
    }

    public String obtainPassword(HttpServletRequest request) {
        String password = null;

        if (verifyHeaderContentType(request)) {
            password = loginBean.getPassword();
        }

        return password;
    }

    protected String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public boolean validateToken(HttpServletRequest request, HttpServletResponse response, String tokenName) {
        boolean isValid = true;

        if (request.getHeader("Accept") != null && request.getHeader(tokenName) == null) {
            isValid = false;
        }
        return isValid;
    }

    public String resolveToken(HttpServletRequest request, HttpServletResponse response, String tokenName) {
        if (validateToken(request, response, tokenName)) {
            return request.getHeader(tokenName);
        } else {
            return null;
        }
    }

    public boolean validateLoginRequest(HttpServletRequest request, HttpServletResponse response) {
        boolean isValid = true;

        if (verifyHeaderContentType(request) && request.getHeader("Accept") != null) {
            response.addHeader("Content-Type", request.getHeader("Accept"));

            try {
                loginBean = requestToLoginBean(request);
            } catch (IOException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                isValid = false;
            }
        }
        return isValid;
    }

    public boolean validateLogin(String username, String password, HttpServletResponse response) {
        boolean isValid = true;

        UsernamePasswordValidationErrors errors = new UsernamePasswordValidationErrors("user");
        if (StringUtils.isEmpty(username)) {
            ValidationUtils.rejectBlank(errors, "username", "Field may not be empty");
        }
        if (StringUtils.isEmpty(password)) {
            ValidationUtils.rejectBlank(errors, "password", "Field may not be empty");
        }

        if (errors.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            try {
                response.getWriter()
                        .append(convertObjectToJson(ValidationUtils.resolveResponse("user", errors)))
                        .flush();
            } catch (IOException e) {
                throw new AuthenticationServiceException("Error generating BAD_REQUEST response", e.getCause());
            }
            isValid = false;
        }
        return isValid;
    }

    protected LoginBean requestToLoginBean(HttpServletRequest request) throws
            IOException {
        StringBuilder stringBuffer = new StringBuilder();
        String line;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }

        return objectMapper.readValue(stringBuffer.toString(), LoginBean.class);
    }

}
