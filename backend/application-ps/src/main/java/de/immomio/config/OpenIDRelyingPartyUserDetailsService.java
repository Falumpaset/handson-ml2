package de.immomio.config;

import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.propertysearcher.entity.customer.PropertySearcherCustomer;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class OpenIDRelyingPartyUserDetailsService implements UserDetailsService {

    private PropertySearcherUserRepository userRepository;

    @Autowired
    private UserSecurityService userSecurityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PropertySearcherUser user = userRepository.findByEmail(username.trim().toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException("username could not be found");
        }

        Collection<BaseAuthority> authorities = userSecurityService.getAuthorities(user);
        return new CustomUserDetails<PropertySearcherUser, PropertySearcherCustomer>(user,
                authorities);
    }

    void setUserRepository(PropertySearcherUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
