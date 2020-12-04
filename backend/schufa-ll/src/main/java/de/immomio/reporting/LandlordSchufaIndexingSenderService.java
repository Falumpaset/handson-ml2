package de.immomio.reporting;

import de.immomio.data.base.type.schufa.SchufaUserInfo;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertySearcherEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.beans.propertysearcher.ReportingSchufaBean;
import de.immomio.reporting.model.event.customer.PropertySearcherEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordSchufaIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    @Autowired
    public LandlordSchufaIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    @Async
    public void schufaRequested(SchufaUserInfo userInfo, PropertySearcherUserProfile psUSer, LandlordUser llUser) {
        ReportingSchufaBean schufaBean = new ReportingSchufaBean(userInfo, psUSer);
        PropertySearcherEvent event = createPropertySearcherEvent(
                psUSer,
                new ReportingEditorBean(llUser),
                PropertySearcherEventType.SCHUFA_REQUEST,
                schufaBean);
        processPropertySearcherIndexing(llUser.getCustomer(), event);
    }

}
