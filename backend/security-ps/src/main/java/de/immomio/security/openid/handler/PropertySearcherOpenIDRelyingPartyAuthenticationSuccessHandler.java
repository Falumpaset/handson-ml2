package de.immomio.security.openid.handler;

import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Maik Kingma
 */

@Component
public class PropertySearcherOpenIDRelyingPartyAuthenticationSuccessHandler extends AbstractOpenIDRelyingPartyAuthenticationSuccessHandler {

    @Autowired
    private PropertySearcherUserRepository userRepository;

    @Override
    public PropertySearcherUserRepository getUserRepository() {
        return userRepository;
    }
}
