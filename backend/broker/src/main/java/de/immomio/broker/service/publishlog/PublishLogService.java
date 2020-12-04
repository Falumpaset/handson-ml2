package de.immomio.broker.service.publishlog;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.property.publish.PublishState;
import de.immomio.model.repository.core.landlord.customer.property.publishlog.BaseLandlordPublishLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */


@Service
public class PublishLogService {

    private final BaseLandlordPublishLogRepository publishLogRepository;

    @Autowired
    public PublishLogService(BaseLandlordPublishLogRepository publishLogRepository) {
        this.publishLogRepository = publishLogRepository;
    }

    public PublishLog savePublishLog(PublishLog publishLog) {
        return publishLogRepository.save(publishLog);
    }

    public PublishLog createPublishLog(Property property) {
        List<Portal> portals = property.getPortals().stream().map(PropertyPortal::getPortal).collect(Collectors.toList());

        PublishLog publishLog = new PublishLog();
        publishLog.setCustomer(property.getCustomer());
        publishLog.setPortals(portals);
        publishLog.setPropertyTask(property.getTask());
        publishLog.setProperty(property);
        publishLog.setAgentInfo(new AgentInfo(property.getUser()));
        publishLog.setPublishState(PublishState.PENDING);

        return publishLogRepository.save(publishLog);
    }
}
