package de.immomio.reporting.service.indexing;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchIndexingService extends AbstractElasticsearchIndexingService{

    @Autowired
    public ElasticsearchIndexingService(RestHighLevelClient client) {
        super(client);
    }
}
