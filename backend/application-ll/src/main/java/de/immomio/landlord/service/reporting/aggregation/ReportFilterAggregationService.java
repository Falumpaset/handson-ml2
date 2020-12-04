package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.landlord.service.reporting.query.AggregationQueryHelper;
import de.immomio.landlord.service.security.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class ReportFilterAggregationService {

    private static final String DEFAULT_AGG = "default_agg";
    private static final String CITY_FIELD = "property.data.address.city.keyword";
    private static final String ZIP_FIELD = "property.data.address.zipCode.keyword";
    private static final String PROPERTY_PREFIX = "property-";
    private static final Integer SIZE = 10000;

    private final RestHighLevelClient client;

    private final UserSecurityService userSecurityService;

    private final AggregationQueryHelper aggregationQueryHelper;

    @Autowired
    public ReportFilterAggregationService(
            RestHighLevelClient client,
            UserSecurityService userSecurityService,
            AggregationQueryHelper aggregationQueryHelper
    ) {
        this.client = client;
        this.userSecurityService = userSecurityService;
        this.aggregationQueryHelper = aggregationQueryHelper;
    }

    public List<String> getCities(List<Long> agents)  {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        SearchRequest request = new SearchRequest(PROPERTY_PREFIX + customer.getId());
        TermsAggregationBuilder cityAgg = AggregationBuilders.terms(DEFAULT_AGG).field(CITY_FIELD).size(SIZE);

        QueryBuilder agentQuery = aggregationQueryHelper.getAgentQuery(null, agents);

        request.source(new SearchSourceBuilder().query(agentQuery).aggregation(cityAgg).size(0));

        return getAggregations(request);
    }

    public List<String> getZipCodes(List<String> cities, List<Long> agents) {

        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        SearchRequest request = new SearchRequest(PROPERTY_PREFIX + customer.getId());
        TermsAggregationBuilder cityAgg = AggregationBuilders.terms(DEFAULT_AGG).field(ZIP_FIELD).size(SIZE);

        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        QueryBuilder agentQuery = aggregationQueryHelper.getAgentQuery(null, agents);
        QueryBuilder cityQuery = aggregationQueryHelper.getCityQuery(null, cities);

        queryBuilder.must(agentQuery).must(cityQuery);
        request.source(new SearchSourceBuilder().query(queryBuilder).aggregation(cityAgg).size(0));

        return getAggregations(request);
    }

    private List<String> getAggregations(SearchRequest request) {
        SearchResponse response;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return Collections.emptyList();
        }
        Terms aggregation = response.getAggregations().get(DEFAULT_AGG);
        Set<String> result = new HashSet<>();
        aggregation.getBuckets().forEach(bucket -> result.add(bucket.getKeyAsString()));

        List<String> sorted = result.stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList());

        return sorted;
    }

}
