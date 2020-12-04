package de.immomio.landlord.listener;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.security.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RepositoryEventHandler(LandlordCustomer.class)
public class LandlordCustomerAfterDeleteListener {

    @Autowired
    private KeycloakService keycloakService;

    @HandleBeforeDelete
    public void deleteRemoteEmail(LandlordUser landlordUser) {
        log.debug("customerAfterDelete-event");

        String email = landlordUser.getEmail();
        if (keycloakService.userExists(email)) {
            keycloakService.removeUser(email);
        }
    }

}
