package de.immomio.importer.worker.security.config;

import de.immomio.model.entity.admin.user.AdminUser;
import de.immomio.model.repository.service.admin.user.AdminUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class OpenIDRelyingPartyUserDetailsService implements UserDetailsService {

    private AdminUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser user = userRepository.findByEmail(username.trim().toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException("username could not be found");
        }

        return user;
    }

    public void setUserRepository(AdminUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
