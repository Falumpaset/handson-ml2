package de.immomio.landlord.listener.user;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.security.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RepositoryEventHandler(LandlordUser.class)
public class LandlordUserListener {

    @Autowired
    private KeycloakService keycloakService;

    @HandleAfterSave
    public void updateUserForSsoService(LandlordUser user) {
        log.info("Update keycloak's user after user is changed. Property id = {}", user.getId());

        List<UserRepresentation> users = keycloakService.searchUser(user.getEmail());
        if (users.size() == 1) {
            UserRepresentation userRepresentation = users.get(0);
            userRepresentation.setFirstName(user.getProfile().getFirstname());
            userRepresentation.setLastName(user.getProfile().getName());
            userRepresentation.setEnabled(user.isEnabled());
            userRepresentation.setEmail(user.getEmail());

            keycloakService.updateUser(userRepresentation);
        } else {
            log.error("User not found in SSO service for updating");
        }
    }

}
