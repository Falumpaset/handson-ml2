package de.immomio.beans.landlord;

import de.immomio.beans.AbstractCustomerUserBean;
import de.immomio.constants.GenderType;
import de.immomio.constants.customer.Title;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.profile.LandlordUserProfile;
import de.immomio.data.shared.bean.common.S3File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Bastian Bliemeister
 */
@Getter
@Setter
@NoArgsConstructor
public class CustomerUserBean extends AbstractCustomerUserBean {

    public CustomerUserBean(LandlordUser landlordUser) {
        LandlordUserProfile profile = landlordUser.getProfile();
        this.gender = profile.getGender();
        this.usertype = landlordUser.getUsertype();
        this.firstname = profile.getFirstname();
        this.name = profile.getName();
        this.email = landlordUser.getEmail();
        this.phone = profile.getPhone();
        this.portrait = profile.getPortrait();
    }

    @NotNull
    private LandlordUsertype usertype;

    private GenderType gender;

    private Title title;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String name;

    @Email
    private String email;

    private String phone;

    private S3File portrait;

    public String getEmail() {
        return email.toLowerCase();
    }
}
