package de.immomio.recipient.reporting;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class RecipientElasticsearchIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    @Autowired
    public RecipientElasticsearchIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    public void applicationCreated(PropertyApplication application, LandlordCustomer customer) {
        indexApplication(application, ApplicationEventType.APPLIED_EXTERNAL, customer);
    }

    private void indexApplication(PropertyApplication application, ApplicationEventType eventType, LandlordCustomer customer) {
        ApplicationEvent event = createApplicationEvent(application, new ReportingEditorBean(application.getUserProfile()), eventType);
        processApplicationIndexing(customer, event);
    }
}
