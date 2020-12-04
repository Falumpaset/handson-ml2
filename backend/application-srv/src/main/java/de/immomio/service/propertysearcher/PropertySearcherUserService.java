package de.immomio.service.propertysearcher;

import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.service.propertysearcher.customer.user.PropertySearcherUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertySearcherUserService {

    private PropertySearcherUserRepository userRepository;

    @Autowired
    public PropertySearcherUserService(PropertySearcherUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<PropertySearcherUserBean> search(String searchTerm, Pageable pageable) {
        Page<PropertySearcherUser> users = userRepository.searchUsers(searchTerm, pageable);

        List<PropertySearcherUserBean> userBeans = users.stream()
                .map(user -> {
                    PropertySearcherUserProfile mainProfile = user.getMainProfile();
                    return PropertySearcherUserBean.builder()
                            .email(user.getEmail()).id(user.getId())
                            .profileData(mainProfile.getData())
                            .prospectOptIn(user.getProspectOptIn().isOptInForProspect())
                            .type(user.getType())
                            .lastLogin(user.getLastLogin())
                            .searchUntil(mainProfile.getSearchUntil())
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(userBeans, users.getPageable(), users.getTotalElements());
    }
}
