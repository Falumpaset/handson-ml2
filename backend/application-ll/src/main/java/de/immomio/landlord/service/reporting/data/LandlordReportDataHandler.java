package de.immomio.landlord.service.reporting.data;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.reporting.model.event.AbstractEvent;
import de.immomio.reporting.model.event.customer.ApplicationEvent;
import de.immomio.reporting.model.event.customer.PropertyNotificationEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import de.immomio.utils.compare.CompareBean;
import de.immomio.utils.compare.CompareWrapper;
import de.immomio.utils.compare.DiffUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.ACCEPTED_AS_TENANT;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.APPLICATION_DELETED;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.APPLICATION_REJECTED;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.APPLIED_EXTERNAL;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.APPLIED_POOL;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.APPOINTMENT_ACCEPTED;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.APPOINTMENT_CANCELED_BY_PS;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.INTENT_GIVEN;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.NO_INTENT_GIVEN;
import static de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType.PROFILE_DATA_CHANGED;
import static de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType.PROPOSAL_CREATED;
import static de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType.PROPOSAL_OFFERED;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordReportDataHandler extends BaseLandlordDataHandler {
    public static final String APPLICATION_USER_PROFILE_ID = "application.user.user_profile_id";
    public static final String APPLICATION_DASH = "application-";
    public static final String PROPOSAL_DASH = "proposal-";
    public static final String PROPERTY_PREFIX = "property";
    public static final String PROPOSAL_USER_PROFILE_ID = "proposal.user.user_profile_id";
    public static final String PROPERTY_ID = "property.id";
    public static final String PROPERTY_DASH = "property-";

    private ElasticsearchQueryHandler queryHandler;

    public LandlordReportDataHandler(UserSecurityService userSecurityService, RestHighLevelClient client, ElasticsearchQueryHandler queryHandler) {
        super(userSecurityService, client);
        this.queryHandler = queryHandler;
    }

    public List<ApplicationEvent> getApplicationHistoryEvents(PropertySearcherUserProfile userProfile) {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        BoolQueryBuilder baseQuery = queryHandler.getBaseQuery(reportingFilterBean, PROPERTY_PREFIX, APPOINTMENT_ACCEPTED, APPOINTMENT_CANCELED_BY_PS, APPLICATION_REJECTED, APPLICATION_DELETED, ACCEPTED_AS_TENANT, INTENT_GIVEN, NO_INTENT_GIVEN, APPLIED_EXTERNAL, APPLIED_POOL);
        baseQuery.must(new TermsQueryBuilder(APPLICATION_USER_PROFILE_ID, String.valueOf(userProfile.getId())));
        SearchRequest request = new SearchRequest(APPLICATION_DASH + getCustomerId());
        return getData(baseQuery, request, ApplicationEvent.class);
    }

    public List<ProposalEvent> getProposalHistoryEvents(PropertySearcherUserProfile userProfile) {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        BoolQueryBuilder baseQuery = queryHandler.getBaseQuery(reportingFilterBean, PROPERTY_PREFIX, PROPOSAL_CREATED, PROPOSAL_OFFERED);
        baseQuery.must(new TermsQueryBuilder(PROPOSAL_USER_PROFILE_ID, String.valueOf(userProfile.getId())));
        SearchRequest request = new SearchRequest(PROPOSAL_DASH + getCustomerId());
        return getData(baseQuery, request, ProposalEvent.class);
    }

    public List<CompareWrapper> getProfileChangedEvents(PropertySearcherUserProfile userProfile) {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        BoolQueryBuilder baseQuery = queryHandler.getBaseQuery(reportingFilterBean, PROPERTY_PREFIX, PROFILE_DATA_CHANGED, APPLIED_POOL, APPLIED_EXTERNAL);
        baseQuery.must(new TermsQueryBuilder(APPLICATION_USER_PROFILE_ID, String.valueOf(userProfile.getId())));
        SearchRequest request = new SearchRequest(APPLICATION_DASH + getCustomerId());
        List<ApplicationEvent> applicationEvents = getData(baseQuery, request, ApplicationEvent.class).stream().sorted(Comparator.comparing(AbstractEvent::getTimestamp).reversed()).collect(Collectors.toList());

        if (applicationEvents.isEmpty()) {
            return Collections.emptyList();
        }

        ApplicationEvent lastApplicationEvent = applicationEvents.get(0);
        List<CompareWrapper> deltas = new ArrayList<>();

        for (ApplicationEvent applicationEvent : applicationEvents) {
            if (lastApplicationEvent.getDocumentId().equals(applicationEvent.getDocumentId())) {
                continue;
            }
            List<CompareBean> differences = DiffUtils.getDifferences(lastApplicationEvent.getApplication().getUser().getProfile(), applicationEvent.getApplication().getUser().getProfile());
            differences.addAll(DiffUtils.getDifferences(lastApplicationEvent.getApplication().getUser().getAddress(),  applicationEvent.getApplication().getUser().getAddress()));
            if (!differences.isEmpty()) {
                List<CompareBean> filteredDiffs = differences.stream().filter(compareBean -> !ArrayList.class.equals(compareBean.getParentClass())).collect(Collectors.toList());

                if (!filteredDiffs.isEmpty()) {
                    deltas.add(new CompareWrapper(applicationEvent.getTimestamp(), filteredDiffs));
                }
            }
            lastApplicationEvent = applicationEvent;
        }

        return deltas;
    }

    public List<PropertyNotificationEvent> getPropertyNotifications(Property property, PropertyEventType eventType) {
        ReportingFilterBean reportingFilterBean = new ReportingFilterBean();
        BoolQueryBuilder baseQuery = queryHandler.getBaseQuery(reportingFilterBean, null, eventType);
        baseQuery.must(new TermsQueryBuilder(PROPERTY_ID, String.valueOf(property.getId())));
        SearchRequest request = new SearchRequest(PROPERTY_DASH + getCustomerId());

        return getData(baseQuery, request, PropertyNotificationEvent.class);
    }

}
