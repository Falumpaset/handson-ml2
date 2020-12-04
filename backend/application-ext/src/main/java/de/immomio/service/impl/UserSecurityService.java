package de.immomio.service.impl;

import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUser;
import de.immomio.data.landlord.entity.user.external.LandlordExternalApiUserDetails;
import de.immomio.model.repository.landlord.customer.user.LandlordExternalApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class UserSecurityService {
    private LandlordExternalApiUserRepository externalApiUserRepository;

    @Autowired
    public UserSecurityService(LandlordExternalApiUserRepository externalApiUserRepository) {
        this.externalApiUserRepository = externalApiUserRepository;
    }

    public LandlordExternalApiUser getPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LandlordExternalApiUserDetails userDetails = (LandlordExternalApiUserDetails) authentication.getPrincipal();
        return externalApiUserRepository.findById(userDetails.getId()).orElseThrow();
    }
}
