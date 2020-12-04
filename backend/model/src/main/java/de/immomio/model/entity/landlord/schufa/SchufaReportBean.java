package de.immomio.model.entity.landlord.schufa;

import de.immomio.data.base.bean.schufa.cbi.creditRating.CreditRatingResponse;
import de.immomio.data.base.bean.schufa.cbi.enums.SchufaReportType;
import de.immomio.data.base.type.schufa.JobState;
import de.immomio.data.base.type.schufa.SchufaUserInfo;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class SchufaReportBean implements Serializable {

    private static final long serialVersionUID = -4463003703428272081L;

    private Long id;
    private Long userProfileId;
    private Date date;
    private AgentInfo agentInfo;
    private SchufaUserInfo userInfo;
    private SchufaReportType type;
    private JobState state;
    private CreditRatingResponse creditRating;

    public SchufaReportBean(LandlordSchufaJob landlordSchufaJob) {
        this.id = landlordSchufaJob.getId();
        this.date = landlordSchufaJob.getCreated();
        this.userInfo = landlordSchufaJob.getSchufaUserInfo();
        this.type = SchufaReportType.convertActionType(landlordSchufaJob.getType());
        this.agentInfo = landlordSchufaJob.getAgentInfo();
        this.state = landlordSchufaJob.getState();
        this.creditRating = landlordSchufaJob.getCreditRatingResponse();
        PropertySearcherUserProfile userProfile = landlordSchufaJob.getUserProfile();
        this.userProfileId = userProfile != null ? userProfile.getId() : null;
    }
}
