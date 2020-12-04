package de.immomio.landlord.service.user;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUserOverviewBean;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordUserConverter {
    public LandlordUserOverviewBean convertUserToUserOverviewBean(LandlordUser user) {
        LandlordUserOverviewBean overviewBean = new LandlordUserOverviewBean();
        overviewBean.setAddress(user.getCustomer().getAddress());
        overviewBean.setCustomerId(user.getCustomer().getId());
        overviewBean.setEmail(user.getEmail());
        overviewBean.setEnabled(user.isEnabled());
        overviewBean.setId(user.getId());
        overviewBean.setProfile(user.getProfile());
        overviewBean.setUserType(user.getUsertype());
        return overviewBean;
    }
}
