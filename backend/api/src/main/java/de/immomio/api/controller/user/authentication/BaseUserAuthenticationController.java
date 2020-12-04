package de.immomio.api.controller.user.authentication;

import de.immomio.api.controller.user.beans.LoginBean;
import de.immomio.api.controller.user.beans.LoginUrl;
import de.immomio.api.controller.user.beans.LogoutBean;
import de.immomio.api.controller.user.beans.LogoutUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@RequestMapping(value = "/users/auth")
public abstract class BaseUserAuthenticationController {

    @Value("${auth.openid.scheme}")
    private String scheme;

    @Value("${auth.openid.host}")
    private String host;

    @Value("${auth.openid.port}")
    private int port;

    @Value("${auth.openid.login_path}")
    private String loginPath;

    @Value("${auth.openid.logout_path}")
    private String logoutPath;

    @Value("${auth.openid.client_id}")
    private String clientId;

    @Value("${auth.openid.response_type}")
    private String responseType;

    @Value("${auth.openid.reponse_mode}")
    private String responseMode;

    @Value("${auth.openid.scope}")
    private String scope;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public abstract ResponseEntity<EntityModel<Object>> federatedLogin(@RequestBody @Valid LoginBean login,
                                                                       HttpServletRequest request,
                                                                       HttpServletResponse response);

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<EntityModel<Object>> federatedLogout(@RequestBody @Valid LogoutBean logout) {
        LogoutUrl redirectUrl = new LogoutUrl(scheme, host, port, logoutPath, logout.getRedirectUri());
        return new ResponseEntity<>(new EntityModel<>(redirectUrl), HttpStatus.OK);
    }

    protected LoginUrl getLoginUrl(LoginBean login) {
        LoginUrl redirectUrl = new LoginUrl(scheme, host, port,
                loginPath, clientId, login.getRedirectUri(), responseType, responseMode, scope,
                login.getNonce(), login.getState(), login.getLoginMethod().getKcIdpHint(), login.getPrompt());

        redirectUrl.setLoginHint(login.getEmail());

        return redirectUrl;
    }
}