package de.immomio.importer.worker.service.reporting;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.event.customer.PropertyEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class ImporterWorkerIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    private static final String IMPORTED = "IMPORTED";

    @Autowired
    public ImporterWorkerIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    public void propertyCreated(Property property, LandlordCustomer customer) {
        indexProperty(property, PropertyEventType.PROPERTY_CREATED, customer);
    }

    public void propertyUpdated(Property property, LandlordCustomer customer) {
        indexProperty(property, PropertyEventType.PROPERTY_UPDATED, customer);
    }

    private void indexProperty(Property property, PropertyEventType eventType, LandlordCustomer customer) {
        ReportingEditorBean editor = new ReportingEditorBean();
        editor.setFirstName(IMPORTED);
        editor.setName(IMPORTED);

        PropertyEvent event = createPropertyEvent(property, editor, eventType);
        processPropertyIndexing(customer, event);
    }
}
