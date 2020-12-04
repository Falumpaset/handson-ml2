package de.immomio.reporting.service.indexing;

import de.immomio.reporting.model.messaging.IndexingCustomerMessageContainer;
import de.immomio.reporting.model.messaging.IndexingEvent;
import de.immomio.reporting.model.messaging.IndexingMessageContainer;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class AbstractElasticsearchIndexingService {
    protected static final String DEFAULT = "default";

    protected final RestHighLevelClient client;

    @Autowired
    public AbstractElasticsearchIndexingService(RestHighLevelClient client) {
        this.client = client;
    }

    public void processIndexing(IndexingMessageContainer container) {
        processHistoryIndexing(container.getEvents(), container.getIndexPrefix());
    }

    protected synchronized void processHistoryIndexing(List<IndexingEvent> events, String index) {
        BulkRequest bulkRequest = new BulkRequest();
        events.forEach(event -> {
            IndexRequest request = getIndexRequest(event, index);
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

    protected IndexRequest getIndexRequest(IndexingEvent event, String index) {
        IndexRequest request = new IndexRequest(index);
        request.type(DEFAULT);
        request.id(UUID.randomUUID().toString());
        request.source(event.getContent(), XContentType.JSON);
        return request;
    }
}
