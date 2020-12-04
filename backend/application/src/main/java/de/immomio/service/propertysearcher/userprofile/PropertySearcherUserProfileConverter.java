package de.immomio.service.propertysearcher.userprofile;

import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.service.propertysearcher.PropertySearcherOfflineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertySearcherUserProfileConverter {

    private final PropertySearcherOfflineUserService offlineUserService;

    @Autowired
    public PropertySearcherUserProfileConverter(PropertySearcherOfflineUserService offlineUserService) {
        this.offlineUserService = offlineUserService;
    }

    public PropertySearcherUserProfileBean convertUserProfile(PropertySearcherUserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        return PropertySearcherUserProfileBean.builder()
                .id(userProfile.getId())
                .address(userProfile.getAddress())
                .usertype(userProfile.getUser().getType())
                .type(userProfile.getType())
                .email(userProfile.getEmail())
                .data(userProfile.getData())
                .itpCustomerName(userProfile.getInternalPoolCustomerName())
                .offlineUser(offlineUserService.isOfflineProfile(userProfile))
                .build();
    }

}
