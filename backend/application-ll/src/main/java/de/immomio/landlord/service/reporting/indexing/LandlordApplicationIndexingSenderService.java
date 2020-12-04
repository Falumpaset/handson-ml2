package de.immomio.landlord.service.reporting.indexing;

import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.property.dk.DkApproval;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.landlord.customer.property.dk.DkApprovalRepository;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Service
public class LandlordApplicationIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    private DkApprovalRepository approvalRepository;

    @Autowired
    public LandlordApplicationIndexingSenderService(RabbitTemplate template, DkApprovalRepository approvalRepository) {
        super(template);
        this.approvalRepository = approvalRepository;
    }

    @Async
    public void askedForIntent(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.ASKED_FOR_INTENT, user);
    }

    @Async
    public void dkLevelChanged(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.DK_LEVEL_CHANGED, user);
    }

    @Async
    public void applicationAccepted(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.APPLICATION_ACCEPTED, user);
    }

    @Async
    public void applicationRejected(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.APPLICATION_REJECTED, user);
    }

    @Async
    public void acceptedAsTenant(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.ACCEPTED_AS_TENANT, user);
    }

    @Async
    public void askedForSelfDisclosure(PropertyApplication application, LandlordUser user) {
        indexApplication(application, ApplicationEventType.ASKED_FOR_SD, user);
    }

    private void indexApplication(PropertyApplication application, ApplicationEventType eventType, LandlordUser user) {
        ApplicationEvent event = createApplicationEvent(application, new ReportingEditorBean(user), eventType);
        provideReferencedData(event);
        processApplicationIndexing(user.getCustomer(), event);
    }

    private void provideReferencedData(ApplicationEvent event) {
        List<DkApproval> dkApprovals = approvalRepository.findByApplicationId(event.getApplication().getId());
        event.getApplication().setDkLevel(dkApprovals.stream()
                .max(Comparator.comparing(DkApproval::getCreated))
                .map(DkApproval::getLevel)
                .orElse(DkApprovalLevel.DK1));
    }

}
