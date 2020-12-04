package de.immomio.service.landlord.property.tenant;

import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.shared.bean.tenant.PropertyTenantBean;
import de.immomio.data.shared.entity.property.TenantInfo;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.service.landlord.property.PropertyConverter;
import de.immomio.service.propertysearcher.userprofile.PropertySearcherUserProfileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertyTenantConverter {

    private final PropertyConverter propertyConverter;
    private final PropertySearcherUserProfileConverter userProfileConverter;

    @Autowired
    public PropertyTenantConverter(PropertyConverter propertyConverter,
            PropertySearcherUserProfileConverter userProfileConverter) {
        this.propertyConverter = propertyConverter;
        this.userProfileConverter = userProfileConverter;
    }

    public PropertyTenantBean convertToTenantBean(PropertyTenant tenant) {
        PropertySearcherUserProfileBean userProfileBean = userProfileConverter.convertUserProfile(tenant.getUserProfile());
        if (userProfileBean == null) {
            PropertySearcherUserProfileData data = new PropertySearcherUserProfileData();
            TenantInfo tenantInfo = tenant.getTenantInfo();
            data.setFirstname(tenantInfo.getFirstName());
            data.setName(tenantInfo.getName());
            userProfileBean = PropertySearcherUserProfileBean.builder().data(data).build();
        }

        return PropertyTenantBean.builder().property(propertyConverter.convertToPropertyBean(tenant.getProperty())).userProfile(userProfileBean).build();
    }
}
