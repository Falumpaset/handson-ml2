package de.immomio.service.reporting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.reporting.model.event.AbstractEvent;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.ParsedAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class BaseElasticsearchDataHandler {

    protected RestHighLevelClient client;

    public BaseElasticsearchDataHandler(RestHighLevelClient client) {
        this.client = client;
    }

    protected static final String DEFAULT = "default";
    private static final int SIZE = 0;

    protected ParsedAggregation performQuery(SearchRequest request,
            AggregationBuilder aggregation,
            QueryBuilder queryBuilder,
            String name
    ) throws IOException {
        request.source(new SearchSourceBuilder().query(queryBuilder).aggregation(aggregation).size(SIZE));
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            return response.getAggregations().get(name);
        } catch (ElasticsearchException e) {
            log.error(e.getMessage(), e);
        }

        return new ParsedDateHistogram();
    }

     protected SearchHits performQuery(SearchRequest request, QueryBuilder queryBuilder, int size) throws IOException {
        request.source(new SearchSourceBuilder().query(queryBuilder).size(size));
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            return response.getHits();
        } catch (ElasticsearchException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    protected  <T extends AbstractEvent> List<T> getData(QueryBuilder baseQuery, SearchRequest request, Class<T> clazz) {
        try {
            SearchHits searchHits = performQuery(request, baseQuery, 10000);
            return Arrays.stream(searchHits.getHits()).map(hit -> {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                try {
                    T t = objectMapper.readValue(hit.getSourceAsString(), clazz);
                    t.setDocumentId(hit.getId());
                    return t;
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }


}
