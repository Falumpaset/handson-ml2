package de.immomio.broker.service.reporting;

import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.dk.DkApproval;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.model.repository.core.landlord.customer.property.dk.BaseDkApprovalRepository;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author Niklas Lindemann
 */

@Service
public class BrokerElasticsearchIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    private static final String BROKER = "BROKER";

    private final BaseDkApprovalRepository baseDkApprovalRepository;

    @Autowired
    public BrokerElasticsearchIndexingSenderService(RabbitTemplate template, BaseDkApprovalRepository baseDkApprovalRepository) {
        super(template);
        this.baseDkApprovalRepository = baseDkApprovalRepository;
    }

    @Async
    public void indexProposal(ProposalEventType eventType, PropertyProposal... proposals) {
        if (proposals.length == 0) {
            return;
        }

        Map<LandlordCustomer, List<PropertyProposal>> customerProposals = Arrays.stream(proposals)
                .collect(groupingBy(propertyProposal -> propertyProposal.getProperty().getCustomer()));

        customerProposals.forEach((customer, propertyProposals) -> {
            ProposalEvent[] events = propertyProposals.stream().map(propertyProposal -> {
                ReportingEditorBean editor = new ReportingEditorBean();
                editor.setFirstName(BROKER);
                editor.setName(BROKER);
                return createProposalEvent(propertyProposal, editor, eventType);
            }).toArray(ProposalEvent[]::new);

            processProposalIndexing(customer, events);
        });
    }

    @Async
    public void applicationRejected(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.APPLICATION_REJECTED, user);
    }

    private void indexApplication(PropertyApplication application, ApplicationEventType eventType, LandlordUser user) {
        ApplicationEvent event = createApplicationEvent(application, new ReportingEditorBean(user), eventType);
        provideReferencedData(event);
        processApplicationIndexing(user.getCustomer(), event);
    }

    private void provideReferencedData(ApplicationEvent event) {
        List<DkApproval> dkApprovals = baseDkApprovalRepository.findByApplicationId(event.getApplication().getId());
        event.getApplication().setDkLevel(dkApprovals.stream()
                .max(Comparator.comparing(DkApproval::getCreated))
                .map(DkApproval::getLevel)
                .orElse(DkApprovalLevel.DK1));
    }

}
