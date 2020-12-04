package de.immomio.service.proposal;

import de.immomio.data.shared.bean.proposal.LandlordPropertyProposalBean;
import de.immomio.data.shared.bean.proposal.PropertyProposalBean;
import de.immomio.data.shared.bean.proposal.PropertySearcherPropertyProposalBean;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.service.landlord.property.PropertyConverter;
import de.immomio.service.propertysearcher.userprofile.PropertySearcherUserProfileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyProposalConverter {

    private PropertyConverter propertyConverter;
    private PropertySearcherUserProfileConverter userProfileConverter;

    @Autowired
    public PropertyProposalConverter(PropertyConverter propertyConverter,
            PropertySearcherUserProfileConverter userProfileConverter) {
        this.propertyConverter = propertyConverter;
        this.userProfileConverter = userProfileConverter;
    }

    public PropertySearcherPropertyProposalBean convertToPropertySearcherProposalBean(PropertyProposal proposal) {
        PropertySearcherPropertyProposalBean proposalBean = new PropertySearcherPropertyProposalBean();
        fillBaseData(proposalBean, proposal);
        proposalBean.setProperty(propertyConverter.convertToPropertySearcherPropertyBean(proposal.getProperty()));
        return proposalBean;
    }

    public LandlordPropertyProposalBean convertToLandlordProposalBean(PropertyProposal proposal) {
        LandlordPropertyProposalBean proposalBean = new LandlordPropertyProposalBean();
        fillBaseData(proposalBean, proposal);
        return proposalBean;
    }

    private void fillBaseData(PropertyProposalBean proposalBean, PropertyProposal proposal) {
        proposalBean.setId(proposal.getId());
        proposalBean.setCustomQuestionScore(proposal.getCustomQuestionScore());
        proposalBean.setScore(proposal.getScore());
        proposalBean.setState(proposal.getState());
        proposalBean.setUserProfile(userProfileConverter.convertUserProfile(proposal.getUserProfile()));
        proposalBean.setProperty(propertyConverter.convertToPropertyBean(proposal.getProperty()));
        proposalBean.setInInternalPool(proposal.getProperty().getCustomer().equals(proposal.getUserProfile().getTenantPoolCustomer()));
    }
    public LandlordPropertyProposalBean convertToProposalBean(PropertyProposal proposal, Date firstSearchProfileCreated) {
        LandlordPropertyProposalBean propertyProposalBean = convertToLandlordProposalBean(proposal);
        propertyProposalBean.setFirstSearchProfileCreated(firstSearchProfileCreated);
        return propertyProposalBean;
    }
}
