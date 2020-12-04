package de.immomio.landlord.service.security;

import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.caching.LandlordCachingService;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.security.AbstractSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */

@Service
public class UserSecurityService extends AbstractSecurityService<LandlordUser, LandlordUserRepository> {

    private final LandlordUserRepository userRepository;

    private final LandlordCachingService landlordCachingService;

    private final PropertyRepository propertyRepository;

    @Autowired
    public UserSecurityService(
            LandlordUserRepository userRepository,
            LandlordCachingService landlordCachingService,
            PropertyRepository propertyRepository
    ) {
        this.userRepository = userRepository;
        this.landlordCachingService = landlordCachingService;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public LandlordUserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public LandlordUser getPrincipal() {
        CustomUserDetails userDetails = (CustomUserDetails) getAuthentication().getPrincipal();
        LandlordUser user = userRepository.loadById(userDetails.getId());
        user.setAuthorities(getAuthorities(user));

        return user;
    }

    public LandlordUser getPrincipalUser() {
        CustomUserDetails userDetails = (CustomUserDetails) getAuthentication().getPrincipal();
        return userRepository.loadById(userDetails.getId());
    }

    public boolean allowUserToReadProperty(Property property, Long propertySearcherUserId) {
        return property != null &&
               property.getPropertyApplications()
                       .stream()
                       .anyMatch(propertyApplication ->
                                         propertyApplication.getUserProfile().getUser().getId().equals(propertySearcherUserId));
    }

    public boolean allowUserToReadProperty(Long id) {
        Property property = propertyRepository.findById(id).get();
        return property.getCustomer() == getPrincipalUser().getCustomer();
    }

    public boolean allowedToReadApplication(PropertyApplication application) {
        return getPrincipalUser().getCustomer().equals(application.getProperty().getCustomer());
    }

    public boolean hasPropertyDeleteRight(Property property) {
        LandlordUsertype usertype = getPrincipalUser().getUsertype();
        boolean isIdsEqual = property.getCustomer().getId().equals(getPrincipal().getCustomer().getId());
        boolean isUserAdminOrEmployee = usertype.equals(LandlordUsertype.COMPANYADMIN)
                || usertype.equals(LandlordUsertype.EMPLOYEE);

        return isIdsEqual && isUserAdminOrEmployee;
    }

    public boolean isCompanyAdmin() {
        return getPrincipalUser().getUsertype().equals(LandlordUsertype.COMPANYADMIN);
    }

    public String getPrincipalEmail() {
        return getPrincipal().getEmail();
    }

    public Collection<BaseAuthority> getAuthorities(LandlordUser user) {
        return landlordCachingService.getAuthorities(user.getId());
    }

}
