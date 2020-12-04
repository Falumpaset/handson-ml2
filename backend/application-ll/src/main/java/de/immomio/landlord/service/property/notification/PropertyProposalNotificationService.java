package de.immomio.landlord.service.property.notification;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.messaging.container.mail.PropertySearcherProfileMailBrokerContainer;
import de.immomio.service.landlord.AbstractNotificationService;
import de.immomio.service.shared.EmailModelProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PropertyProposalNotificationService extends AbstractNotificationService {

    private static final String PROPERTY_NEW_FOR_PROSPECT_SUBJECT_KEY = "property.new.for.prospect.subject";

    private final EmailModelProvider emailModelProvider;

    @Autowired
    public PropertyProposalNotificationService(EmailModelProvider emailModelProvider, RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
        this.emailModelProvider = emailModelProvider;
    }

    public void invitedForProposal(PropertyProposal proposal) {
        PropertySearcherUserProfile userProfile = proposal.getUserProfile();
        Property property = proposal.getProperty();
        Map<String, Object> model = emailModelProvider.createUserProfilePropertyModel(userProfile, property);
        model.put(ModelParams.PROPOSAL_ID, proposal.getId());
        PropertySearcherProfileMailBrokerContainer container = new PropertySearcherProfileMailBrokerContainer(userProfile.getId(),
                MailTemplate.NEW_PROPERTY_FOR_PROSPECT, PROPERTY_NEW_FOR_PROSPECT_SUBJECT_KEY, model,
                property.getCustomer().getId());

        sendPropertySearcherEmailMessage(container, userProfile);
    }
}
