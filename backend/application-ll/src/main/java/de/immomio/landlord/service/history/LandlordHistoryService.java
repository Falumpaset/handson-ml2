package de.immomio.landlord.service.history;

import de.immomio.beans.shared.PropertySearcherHistoryBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.landlord.service.reporting.data.LandlordReportDataHandler;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.utils.compare.CompareWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordHistoryService {

    private final LandlordReportDataHandler reportDataHandler;

    private final PropertyRepository propertyRepository;

    @Autowired
    public LandlordHistoryService(
            LandlordReportDataHandler reportDataHandler,
            PropertyRepository propertyRepository) {
        this.reportDataHandler = reportDataHandler;
        this.propertyRepository = propertyRepository;
    }

    public List<PropertySearcherHistoryBean> getHistory(PropertySearcherUserProfile userProfile) {
        List<CompareWrapper> diffs = reportDataHandler.getProfileChangedEvents(userProfile);

        List<ApplicationEvent> applicationEvents = reportDataHandler.getApplicationHistoryEvents(userProfile);

        List<PropertySearcherHistoryBean> historyBeans = applicationEvents.stream().map(applicationEvent -> {
            Property property = propertyRepository.findById(applicationEvent.getApplication().getProperty().getId()).orElse(null);
            return property == null
                    ? new PropertySearcherHistoryBean(null, null, applicationEvent.getEventType(), applicationEvent.getTimestamp(), applicationEvent.getApplication().getProperty().getData(), true, applicationEvent.getApplication().getPortal(), null)
                    : new PropertySearcherHistoryBean(property, applicationEvent.getEventType(), applicationEvent.getTimestamp(), false, property.getData(), applicationEvent.getApplication().getPortal());
        }).collect(Collectors.toList());

        List<ProposalEvent> proposalEvents = reportDataHandler.getProposalHistoryEvents(userProfile);
        historyBeans.addAll(proposalEvents.stream().map(proposalEvent -> {
            try {
                Property property = propertyRepository.findById(proposalEvent.getProposal().getProperty().getId()).orElse(null);
                return property == null
                        // ? new PropertySearcherHistoryBean(null, null, proposalEvent.getEventType(), proposalEvent.getTimestamp(), proposalEvent.getProposal().getProperty().getData(), true, null, null)
                        ? null // TODO remove after elasticsearch cleanup
                        : new PropertySearcherHistoryBean(property, proposalEvent.getEventType(),
                        proposalEvent.getTimestamp(), false, property.getData(), null);
            } catch (AccessDeniedException e) {
                log.error("Faulty proposal event", e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList()));

        historyBeans.addAll(diffs.stream().map(compareWrapper -> new PropertySearcherHistoryBean(null, null, ApplicationEventType.PROFILE_DATA_CHANGED, compareWrapper.getDate(), null, null, null, compareWrapper.getCompareBeans())).collect(Collectors.toList()));
        return historyBeans.stream()
                .sorted(Comparator.comparing(PropertySearcherHistoryBean::getCreated).reversed())
                .collect(Collectors.toList());
    }

}
