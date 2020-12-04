package de.immomio.reporting.service.indexing;

import de.immomio.reporting.model.messaging.CustomerIndexingEvent;
import de.immomio.reporting.model.messaging.IndexingCustomerMessageContainer;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class ElasticsearchCustomerIndexingService extends AbstractElasticsearchIndexingService {

    private static final String CURRENT_INDEX_PREFIX = "current-";

    @Autowired
    public ElasticsearchCustomerIndexingService(RestHighLevelClient client) {
        super(client);
    }

    public void processIndexing(IndexingCustomerMessageContainer container) {
        processHistoryIndexing(container.getCustomerId(), container.getEvents(), container.getIndexPrefix());
        processCurrentIndexing(container.getCustomerId(), container.getEvents(), container.getIndexPrefix());
    }

    private void processCurrentIndexing(Long customerId, List<CustomerIndexingEvent> events, String indexPrefix) {

        BulkRequest bulkRequest = new BulkRequest();
        events.forEach(event -> {
            UpdateRequest updateRequest = getUpdateRequest(customerId, event, indexPrefix);
            bulkRequest.add(updateRequest);
        });
        try {
            RequestOptions defaultOptions = RequestOptions.DEFAULT;
            log.info("bulk request " + bulkRequest.requests().size());
            client.bulk(bulkRequest, defaultOptions);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    private synchronized void processHistoryIndexing(Long customerId, List<CustomerIndexingEvent> events, String indexPrefix) {
        BulkRequest bulkRequest = new BulkRequest();
        events.forEach(event -> {
            IndexRequest request = getIndexRequest(customerId, event, indexPrefix);
            bulkRequest.add(request);
        });
        try {
            RequestOptions defaultOptions = RequestOptions.DEFAULT;
            log.info("indexing " + bulkRequest.requests().size() + " documents");
            client.bulk(bulkRequest, defaultOptions);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
    }

    private UpdateRequest getUpdateRequest(Long customerId, CustomerIndexingEvent event, String indexPrefix) {
        IndexRequest indexRequest = new IndexRequest(indexPrefix + CURRENT_INDEX_PREFIX + customerId);
        indexRequest.type(DEFAULT);
        indexRequest.id(event.getId().toString());
        indexRequest.source(event.getContent(), XContentType.JSON);
        UpdateRequest updateRequest = new UpdateRequest(
                indexPrefix + CURRENT_INDEX_PREFIX + customerId, DEFAULT,
                event.getId().toString());
        updateRequest.doc(indexRequest);
        updateRequest.upsert(indexRequest);
        return updateRequest;
    }

    private IndexRequest getIndexRequest(Long customerId, CustomerIndexingEvent event, String indexPrefix) {
        return getIndexRequest(event, indexPrefix + customerId);
    }

}