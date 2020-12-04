package de.immomio.security.auditing;

import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.security.openid.token.OpenIDRelyingPartyToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Component
public class SystemLoggedInUserAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            OpenIDRelyingPartyToken authentication = (OpenIDRelyingPartyToken) SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails)  authentication.getPrincipal();
            return Optional.of(userDetails.getEmail());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
