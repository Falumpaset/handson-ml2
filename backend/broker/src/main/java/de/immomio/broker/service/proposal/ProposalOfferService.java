package de.immomio.broker.service.proposal;

import de.immomio.broker.service.MailMessageService;
import de.immomio.broker.service.reporting.BrokerElasticsearchIndexingSenderService;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposalState;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Service
public class ProposalOfferService {

    private final BasePropertyProposalRepository propertyProposalRepository;

    private final MailMessageService mailMessageService;

    private final EmailModelProvider emailModelProvider;

    private final BrokerElasticsearchIndexingSenderService elasticsearchIndexingService;

    private static final String PROPERTY_NEW_FOR_PROSPECT_SUBJECT_KEY = "property.new.for.prospect.subject";

    @Autowired
    public ProposalOfferService(
            BasePropertyProposalRepository propertyProposalRepository,
            MailMessageService mailMessageService,
            EmailModelProvider emailModelProvider,
            BrokerElasticsearchIndexingSenderService elasticsearchIndexingService
    ) {
        this.propertyProposalRepository = propertyProposalRepository;
        this.mailMessageService = mailMessageService;
        this.emailModelProvider = emailModelProvider;
        this.elasticsearchIndexingService = elasticsearchIndexingService;
    }

    public void offerProposalIfNecessary(PropertyProposal proposal) {
        if (proposalShouldBeOffered(proposal)) {
            offerProposal(proposal);
        }
    }

    public boolean proposalShouldBeOffered(PropertyProposal proposal) {
        Property property = proposal.getProperty();
        return proposal.getState() == PropertyProposalState.PROSPECT &&
                property.isAutoOfferEnabled() &&
                proposal.getScore() >= property.getAutoOfferThreshold();
    }


    public void inviteForProposal(PropertyProposal proposal) {
        PropertySearcherUserProfile userProfile = proposal.getUserProfile();
        Property property = proposal.getProperty();
        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(userProfile, property);
        model.put(ModelParams.PROPOSAL_ID, proposal.getId());

        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(userProfile.getId(),
                MailTemplate.NEW_PROPERTY_FOR_PROSPECT, PROPERTY_NEW_FOR_PROSPECT_SUBJECT_KEY, model,
                property.getCustomer().getId());

        mailMessageService.onMessage(container);
    }

    private void offerProposal(PropertyProposal proposal) {
        propertyProposalRepository.customSetOffered(proposal.getId());
        elasticsearchIndexingService.indexProposal(ProposalEventType.PROPOSAL_OFFERED, proposal);
        inviteForProposal(proposal);
    }
}
