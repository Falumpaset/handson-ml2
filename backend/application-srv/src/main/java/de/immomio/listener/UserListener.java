package de.immomio.listener;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.security.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */
@Component
@RepositoryEventHandler
public class UserListener {

    public static final String REALM = "immomio";
    private final KeycloakService keycloakService;

    @Autowired
    public UserListener(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @HandleAfterCreate
    public void createKeycloakUser(LandlordUser user) {
        keycloakService.createUser(
                REALM,
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getProfile().getFirstname(),
                user.getProfile().getName());

    }
}
