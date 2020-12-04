package de.immomio.reporting.model.beans.propertysearcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportingPropertySearcherBean {

    private PropertySearcherUserProfileData profile;
    @JsonProperty("user_profile_type")
    private PropertySearcherUserProfileType userProfileType;
    @JsonProperty("user_type")
    private PropertySearcherUserType userType;
    private Date searchUntil;
    private Date created;
    private Address address;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_profile_id")
    private Long userProfileId;
    private Date emailVerified;
    private Date lastLogin;
    private String email;
    private boolean enabled;

    public ReportingPropertySearcherBean(PropertySearcherUserProfile userProfile) {
        this.profile = userProfile.getData();
        this.userProfileType = userProfile.getType();
        this.searchUntil = userProfile.getSearchUntil();
        this.created = userProfile.getCreated();
        this.address = userProfile.getAddress();
        this.userId = userProfile.getUser().getId();
        this.userProfileId = userProfile.getId();
        this.emailVerified = userProfile.getUser().getEmailVerified();
        this.lastLogin = userProfile.getUser().getLastLogin();
        this.email = userProfile.getEmail();
        this.enabled = userProfile.getUser().isEnabled();
        this.userType = userProfile.getUser().getType();
    }
}
