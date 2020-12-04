package de.immomio.landlord.service.external;

import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUser;
import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUserBean;
import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUserCreatedBean;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.user.LandlordExternalApiUserRepository;
import de.immomio.security.service.KeycloakService;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class ExternalApiUserService {

    private final KeycloakService keycloakService;

    private LandlordExternalApiUserRepository externalApiUserRepository;

    private final UserSecurityService userSecurityService;


    @Autowired
    public ExternalApiUserService(KeycloakService keycloakService,
                                  LandlordExternalApiUserRepository externalApiUserRepository, UserSecurityService userSecurityService) {
        this.keycloakService = keycloakService;
        this.externalApiUserRepository = externalApiUserRepository;
        this.userSecurityService = userSecurityService;
    }

    public LandlordExternalApiUserCreatedBean createApiUser() {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(48, 90)
                .build();

        String username;

        Optional<LandlordExternalApiUser> existingUser;
        do {
            username = pwdGenerator.generate(15);
            existingUser = externalApiUserRepository.findFirstByUsername(username.toLowerCase());
        } while (existingUser.isPresent());

        String password = pwdGenerator.generate(20);
        keycloakService.createUser("immomio", username, username, null, null, password, true);

        LandlordExternalApiUser externalApiUser = new LandlordExternalApiUser();
        externalApiUser.setCustomer(userSecurityService.getPrincipalUser().getCustomer());
        externalApiUser.setUsername(username);
        LandlordExternalApiUser savedUser = externalApiUserRepository.save(externalApiUser);
        return LandlordExternalApiUserCreatedBean.builder()
                .username(username)
                .password(password)
                .created(savedUser.getCreated())
                .id(savedUser.getId())
                .build();
    }

    public Page<LandlordExternalApiUserBean> findAll(Pageable pageable) {
        Page<LandlordExternalApiUser> apiUsers = externalApiUserRepository.findAll(pageable);
        List<LandlordExternalApiUserBean> apiUserBeans = apiUsers.getContent().stream()
                .map(apiUser -> LandlordExternalApiUserBean.builder()
                        .username(apiUser.getUsername())
                        .created(apiUser.getCreated())
                        .id(apiUser.getId())
                        .build())
                .collect(Collectors.toList());
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return new PageImpl<>(apiUserBeans, pageRequest, apiUsers.getTotalElements());
    }

    public void delete(LandlordExternalApiUser user) {
        keycloakService.removeUser(user.getUsername());
        externalApiUserRepository.delete(user);
    }
}
