package de.immomio.service.security;

import de.immomio.constants.exceptions.ApiNotFoundException;
import de.immomio.data.base.entity.customer.user.CustomUserDetails;
import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.base.type.user.PropertySearcherUserRightType;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.right.PropertySearcherUsertypeRight;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.propertysearcher.user.PropertySearcherUserRepository;
import de.immomio.model.repository.propertysearcher.user.right.PropertySearcherUserTypeRightRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.security.AbstractSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSecurityService extends AbstractSecurityService<PropertySearcherUser, PropertySearcherUserRepository> {

    private final PropertySearcherUserRepository userRepository;

    private final PropertySearcherUserTypeRightRepository propertySearcherUserTypeRightRepository;

    private final PropertyApplicationRepository propertyApplicationRepository;

    @Autowired
    public UserSecurityService(
            PropertySearcherUserRepository userRepository,
            PropertySearcherUserTypeRightRepository propertySearcherUserTypeRightRepository,
            PropertyApplicationRepository propertyApplicationRepository
    ) {
        this.userRepository = userRepository;
        this.propertySearcherUserTypeRightRepository = propertySearcherUserTypeRightRepository;
        this.propertyApplicationRepository = propertyApplicationRepository;
    }

    @Override
    public PropertySearcherUserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public PropertySearcherUser getPrincipal() {
        CustomUserDetails userDetails = (CustomUserDetails) getAuthentication().getPrincipal();
        PropertySearcherUser user = userRepository.loadById(userDetails.getId());
        user.setAuthorities(getAuthorities(user));
        return user;
    }

    public PropertySearcherUser getPrincipalUser() {
        CustomUserDetails userDetails = (CustomUserDetails) getAuthentication().getPrincipal();
        return userRepository.loadById(userDetails.getId());
    }

    public PropertySearcherUserProfile getPrincipalUserProfile() {
        return getPrincipalUser().getMainProfile();
    }

    public Long getPrincipalId() {
        return getPrincipal().getId();
    }

    //TODO this lookup can be optimized, simple iteration over applications list does not scale
    public boolean maySeeAppointments(Property property, Long userId) {
        List<PropertyApplication> applications = property.getPropertyApplications();
        for (PropertyApplication application : applications) {
            if (application.getUserProfile().getUser().getId().equals(userId) &&
                    application.getStatus().equals(ApplicationStatus.ACCEPTED)) {
                return true;
            }
        }
        return false;
    }

    public boolean maySeeCustomQuestion(CustomQuestion customQuestion, Long userId) {
        List<PropertyApplication> applications = propertyApplicationRepository.findByUserProfileId(userId);

        List<Prioset> priosets = customQuestion.getPriosets()
                .stream()
                .map(PriosetCustomQuestionAssociation::getPrioset)
                .collect(Collectors.toList());

        return applications.stream().map(PropertyApplication::getProperty).map(Property::getPrioset).anyMatch(priosets::contains) || customQuestion.getType() == CustomQuestionType.GLOBAL;
    }

    public boolean allowUserToReadProperty(Property property, Long propertySearcherUserId) {
        return property != null && property.getPropertyApplications().stream()
                .anyMatch(propertyApplication -> propertyApplication.getUserProfile().getUser().getId().equals(propertySearcherUserId));
    }

    public boolean isUserAllowedToReadApplication(Long applicationId) {
        return propertyApplicationRepository.findById(applicationId)
                .orElseThrow(ApiNotFoundException::new)
                .getUserProfile()
                .getUser()
                .equals(getPrincipalUser());
    }

    public Collection<BaseAuthority> getAuthorities(PropertySearcherUser user) {
        PropertySearcherUserRightType userType = user.getUsertype();
        Set<BaseAuthority> authorities = new HashSet<>(user.getCustomer().getAuthorities(userType));

        List<PropertySearcherUsertypeRight> types = propertySearcherUserTypeRightRepository.findAllByUserType(userType);
        if (types == null) {
            return authorities;
        }

        for (PropertySearcherUsertypeRight userTypeRight : types) {
            authorities.add(new BaseAuthority(userTypeRight.getRight().getRight().getShortCode()));
        }

        return authorities;
    }
}
