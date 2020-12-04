package de.immomio.importer.worker.service;


import de.immomio.model.entity.admin.user.AdminUser;
import de.immomio.model.repository.service.admin.user.AdminUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

/**
 * @author Maik Kingma.
 */

@Slf4j
@Service
public class AdminUserService {

    private final AdminUserRepository userRepository;

    public AdminUserService(AdminUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity updateLastLogin(Principal principal) {
        try {
            AdminUser userDetails = (AdminUser) principal;
            AdminUser user = userRepository.findByEmail(userDetails.getEmail());
            user.setLastLogin(new Date());
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
