package de.immomio.propertysearcher.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.immomio.beans.AbstractRegisterUserBean;
import de.immomio.data.propertysearcher.bean.searchprofile.SearchProfileBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseRegisterBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertySearcherRegisterBean
        extends AbstractRegisterUserBean<PropertySearcherCustomerRegisterBean> implements Serializable {

    private static final long serialVersionUID = -3769130535444074564L;

    private boolean optInProspect;

    private boolean socialLogin;

    private Address address;

    private SearchProfileBean searchProfile;

    private Long brandedCustomerId;

    private List<CustomQuestionResponseRegisterBean> customQuestionResponses;

    private PropertySearcherUserProfileData userProfileData;
}
