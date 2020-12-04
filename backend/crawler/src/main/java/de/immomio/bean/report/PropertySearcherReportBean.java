package de.immomio.bean.report;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PropertySearcherReportBean implements Serializable {

    public PropertySearcherReportBean(PropertySearcherUserProfile userProfile) {
        this.setFirstName(userProfile.getData().getFirstname());
        this.setLastName(userProfile.getData().getName());
        this.setEmail(userProfile.getEmail());
        this.setOptInProspect(userProfile.getUser().getProspectOptIn().isOptInForProspect());
    }

    private static final long serialVersionUID = -5385822091177574365L;

    private String firstName;

    private String lastName;

    private String email;

    private boolean optInProspect;
}
