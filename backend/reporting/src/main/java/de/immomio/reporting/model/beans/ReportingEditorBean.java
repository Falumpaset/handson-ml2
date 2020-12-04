package de.immomio.reporting.model.beans;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportingEditorBean implements Serializable {

    private static final long serialVersionUID = -971735507791722268L;

    private Long id;

    private String name;

    private String firstName;

    private String email;

    public ReportingEditorBean(LandlordUser user) {
        this.id = user.getId();
        this.name = user.getProfile().getName();
        this.firstName = user.getProfile().getFirstname();
        this.email = user.getEmail();
    }

    public ReportingEditorBean(PropertySearcherUserProfile userProfile) {
        this.id = userProfile.getId();
        this.name = userProfile.getData().getName();
        this.firstName = userProfile.getData().getFirstname();
        this.email = userProfile.getEmail();
    }
}
