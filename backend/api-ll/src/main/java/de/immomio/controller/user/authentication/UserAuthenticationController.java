package de.immomio.controller.user.authentication;

import de.immomio.api.controller.user.authentication.BaseUserAuthenticationController;
import de.immomio.api.controller.user.beans.LoginBean;
import de.immomio.api.controller.user.beans.LoginUrl;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.user.LandlordChangePasswordService;
import de.immomio.landlord.service.user.LandlordUserService;
import de.immomio.security.service.KeycloakService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserAuthenticationController extends BaseUserAuthenticationController {
    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private LandlordChangePasswordService changePasswordService;

    @Autowired
    private LandlordUserService landlordUserService;

    public ResponseEntity<EntityModel<Object>> federatedLogin(@RequestBody @Valid LoginBean login,
                                                           HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(login.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //TODO remove this for Production
        if (landlordUserService.exists(login.getEmail()) && !keycloakService.userExists(login.getEmail())) {
            LandlordUser user = landlordUserService.findByEmail(login.getEmail());

            keycloakService.createUser("immomio", user.getId() + "", user.getEmail(), user.getProfile().getFirstname(),
                    user.getProfile().getName());
            changePasswordService.resetPassword(landlordUserService.findByEmail(login.getEmail()));
        }

        LoginUrl redirectUrl = getLoginUrl(login);

        return new ResponseEntity<>(new EntityModel<>(redirectUrl), HttpStatus.OK);
    }

}
