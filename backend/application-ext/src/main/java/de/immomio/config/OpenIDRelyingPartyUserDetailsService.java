package de.immomio.config;

import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUser;
import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUserDetails;
import de.immomio.exception.ExternalApiUnauthorizedException;
import de.immomio.model.repository.landlord.customer.user.LandlordExternalApiUserRepository;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OpenIDRelyingPartyUserDetailsService implements UserDetailsService {

    @Setter
    private LandlordExternalApiUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LandlordExternalApiUser user = userRepository.findFirstByUsername(username.trim().toLowerCase()).orElseThrow(ExternalApiUnauthorizedException::new);

        return new LandlordExternalApiUserDetails(user.getUsername(), user.getCustomer(), user.getId());
    }

}
