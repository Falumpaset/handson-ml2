package de.immomio.security.openid.handler;

import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Maik Kingma
 */

@Component
public class LandlordOpenIDRelyingPartyAuthenticationSuccessHandler extends AbstractOpenIDRelyingPartyAuthenticationSuccessHandler {

    @Autowired
    private LandlordUserRepository userRepository;

    @Override
    public LandlordUserRepository getUserRepository() {
        return userRepository;
    }
}
