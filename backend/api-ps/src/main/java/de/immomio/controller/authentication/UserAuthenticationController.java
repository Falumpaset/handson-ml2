package de.immomio.controller.authentication;

import de.immomio.api.controller.user.authentication.BaseUserAuthenticationController;
import de.immomio.api.controller.user.beans.LoginBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.security.openid.LoginMethod;
import de.immomio.security.service.KeycloakService;
import de.immomio.service.propertySearcher.change.PropertySearcherChangePasswordService;
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

    private static final String ORGANIZATION_NAME = "immomio";

    private final KeycloakService keycloakService;

    private final PropertySearcherChangePasswordService changePasswordService;

    private final PropertySearcherUserRepository userRepository;

    @Autowired
    public UserAuthenticationController(KeycloakService keycloakService,
            PropertySearcherChangePasswordService changePasswordService,
            PropertySearcherUserRepository userRepository) {
        this.keycloakService = keycloakService;
        this.changePasswordService = changePasswordService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<EntityModel<Object>> federatedLogin(@RequestBody @Valid LoginBean login, HttpServletRequest request, HttpServletResponse response) {
        if (LoginMethod.DEFAULT == login.getLoginMethod() && StringUtils.isBlank(login.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PropertySearcherUser user = userRepository.findByEmail(login.getEmail());
        if (user != null && user.isRegistered() && !keycloakService.userExists(user.getEmail())) {

            keycloakService.createUser(ORGANIZATION_NAME, String.valueOf(user.getId()), user.getEmail(), user.getMainProfile().getData().getFirstname(),
                    user.getMainProfile().getData().getName());
            changePasswordService.resetPassword(user);
        }

        return new ResponseEntity<>(new EntityModel<>(getLoginUrl(login)), HttpStatus.OK);
    }
}
