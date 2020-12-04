package de.immomio.reporting.model.beans;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.reporting.model.beans.propertysearcher.ReportingPropertySearcherBean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportingPropertyApplicationBean implements Serializable {

    private static final long serialVersionUID = 5927331767056733673L;
    private Long id;
    private Portal portal;
    private Date created;
    private ReportingPropertyBean property;
    private ReportingPropertySearcherBean user;
    private Double score;
    private BigDecimal customQuestionScore;
    private boolean askedForIntent;
    private ApplicationStatus status;
    private String text;

    private DkApprovalLevel dkLevel;

    public ReportingPropertyApplicationBean(PropertyApplication application) {
        this.id = application.getId();
        this.portal = application.getPortal();
        this.created = application.getCreated();
        this.property = new ReportingPropertyBean(application.getProperty());
        this.user = new ReportingPropertySearcherBean(application.getUserProfile());
        this.score = application.getScore();
        this.customQuestionScore = application.getCustomQuestionScore() != null ? application.getCustomQuestionScore().getScoreExcludingRange() : null;
        this.askedForIntent = application.isAskedForIntent();
        this.status = application.getStatus();
        this.text = application.getText();
    }
}
