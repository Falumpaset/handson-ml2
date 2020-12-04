package de.immomio.service.reporting;

import de.immomio.reporting.model.event.customer.ApplicationEvent;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Component
public class SharedDataHandler extends BaseElasticsearchDataHandler {

    @Autowired
    public SharedDataHandler(RestHighLevelClient client) {
        super(client);
    }

    public List<ApplicationEvent> getProfileChangedEvents(Long applicationId, Long customerId) {
        BoolQueryBuilder query = new BoolQueryBuilder();
        query.must(QueryBuilders.matchQuery("eventType", "PROFILE_DATA_CHANGED"));
        query.must(QueryBuilders.matchQuery("application.id", applicationId));
        SearchRequest request = new SearchRequest("application-" + customerId);

        return getData(query, request, ApplicationEvent.class);
    }
}
