package de.immomio.propertysearcher.bean;

import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.shared.bean.common.Address;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
public class EditUserProfileBean {
    private PropertySearcherUserProfileData profile;

    private Address address;
}
