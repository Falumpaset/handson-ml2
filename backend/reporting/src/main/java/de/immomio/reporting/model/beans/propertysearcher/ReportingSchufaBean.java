package de.immomio.reporting.model.beans.propertysearcher;

import de.immomio.data.base.type.schufa.SchufaUserInfo;
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
public class ReportingSchufaBean implements ReportingPropertySearcherIndexable, Serializable {
    private static final long serialVersionUID = -1932280901235358234L;

    private SchufaUserInfo schufaUserInfo;
    private ReportingPropertySearcherBean user;

    public ReportingSchufaBean(SchufaUserInfo userInfo, PropertySearcherUserProfile userProfile) {
        this.schufaUserInfo = userInfo;
        this.user = new ReportingPropertySearcherBean(userProfile);
    }
}
