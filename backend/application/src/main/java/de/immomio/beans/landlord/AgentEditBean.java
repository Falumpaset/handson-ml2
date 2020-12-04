package de.immomio.beans.landlord;

import de.immomio.constants.GenderType;
import de.immomio.constants.customer.Title;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.shared.bean.common.S3File;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Maik Kingma
 */

@Getter
@Setter
public class AgentEditBean {

    private LandlordUsertype usertype;

    private GenderType gender;

    private Title title;

    private String firstname;

    private String name;

    private String phone;

    private S3File portrait;
}
