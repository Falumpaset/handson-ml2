package de.immomio.beans.landlord.proposal;

import de.immomio.controller.paging.CustomPageable;
import de.immomio.data.base.type.user.PropertySearcherUserType;
import de.immomio.data.base.type.user.profile.PropertySearcherUserProfileType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ProposalSearchBean extends CustomPageable implements Serializable {

    private static final long serialVersionUID = -7743630764498395527L;

    private Long propertyId;

    private Long userId;

    private List<PropertyProposalState> states;

    private List<PropertySearcherUserProfileType> userProfileTypes;

    private Boolean wbs;

}
