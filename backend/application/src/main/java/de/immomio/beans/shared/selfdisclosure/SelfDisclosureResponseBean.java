package de.immomio.beans.shared.selfdisclosure;

import de.immomio.data.landlord.bean.property.PropertyBean;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileBean;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelfDisclosureResponseBean implements Serializable {

    private static final long serialVersionUID = -4736185349914610660L;

    private Long id;

    private SelfDisclosureResponseData data;

    private PropertySearcherUserProfileBean user;

    private PropertyBean property;

    private SelfDisclosureBean selfDisclosure;

}
