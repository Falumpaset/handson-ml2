package de.immomio.reporting.model.beans.propertysearcher;

import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.reporting.model.beans.ReportingPropertyBean;
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
public class ReportingProposalBean implements ReportingPropertySearcherIndexable, Serializable {
    private static final long serialVersionUID = -8186487881785539638L;

    private Date created;

    private Date offered;

    private Date accepted;

    private Double score;

    private PropertyProposalState state;

    private ReportingPropertySearcherBean user;

    private ReportingPropertyBean property;

    public ReportingProposalBean(PropertyProposal proposal) {
        this.created = proposal.getCreated();
        this.offered = proposal.getOffered();
        this.accepted = proposal.getAccepted();
        this.score = proposal.getScore();
        this.state = proposal.getState();
        this.property = new ReportingPropertyBean(proposal.getProperty());
        this.user = new ReportingPropertySearcherBean(proposal.getUserProfile());
    }

}
