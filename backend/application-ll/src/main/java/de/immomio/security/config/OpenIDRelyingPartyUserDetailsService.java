/**
 *
 */
package de.immomio.security.config;

import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public class OpenIDRelyingPartyUserDetailsService implements UserDetailsService {

    private LandlordUserRepository userRepository;

    @Autowired
    private UserSecurityService userSecurityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LandlordUser user = userRepository.findByEmail(username.trim().toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException("username could not be found");
        }

        Collection<BaseAuthority> authorities = userSecurityService.getAuthorities(user);
        return new CustomUserDetails<LandlordUser, LandlordCustomer>(user, authorities);
    }

    public void setUserRepository(LandlordUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
