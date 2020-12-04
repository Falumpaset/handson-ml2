package de.immomio.landlord.service.reporting.query;

import de.immomio.reporting.model.filter.ReportingFilterBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Niklas Lindemann
 */

@Component
public class ElasticsearchQueryHandler {

    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String EVENT_TYPE = "eventType";

    private AggregationQueryHelper queryHelper;

    @Autowired
    public ElasticsearchQueryHandler(
            AggregationQueryHelper queryHelper
    ) {
        this.queryHelper = queryHelper;
    }

    public BoolQueryBuilder getBasePrincipalQuery(ReportingFilterBean filterBean, String propertyPrefix, Enum... eventTypes) {
        QueryBuilder principalQuery = getAgentQuery(propertyPrefix, filterBean);

        return getBaseQuery(filterBean, propertyPrefix, eventTypes).must(principalQuery);
    }

    public BoolQueryBuilder getBaseQuery(ReportingFilterBean filterBean, String propertyPrefix, Enum... eventTypes) {
        BoolQueryBuilder eventTypeQuery = new BoolQueryBuilder().minimumShouldMatch(1);
        Arrays.asList(eventTypes).forEach(eventType -> eventTypeQuery.should(QueryBuilders.matchQuery(EVENT_TYPE, eventType)));

        QueryBuilder cityQuery = getCityQuery(propertyPrefix, filterBean);
        QueryBuilder zipCodeQuery = getZipCodeQuery(propertyPrefix, filterBean);

        return QueryBuilders.boolQuery().must(eventTypeQuery).must(cityQuery).must(zipCodeQuery);
    }


    public BoolQueryBuilder getBaseDateRangeQuery(ReportingFilterBean filterBean, String propertyPrefix, Enum... eventTypes) {
        return getBaseDateRangeQuery(filterBean, propertyPrefix, TIMESTAMP_FIELD, eventTypes);
    }

    public BoolQueryBuilder getBaseDateRangeQuery(ReportingFilterBean filterBean, String propertyPrefix, String rangeField, Enum... eventTypes) {
        RangeQueryBuilder dateRangeQuery = QueryBuilders
                .rangeQuery(rangeField)
                .gte(filterBean.getStart().getTime())
                .lte(filterBean.getEnd().getTime());

        return QueryBuilders.boolQuery().must(getBasePrincipalQuery(filterBean, propertyPrefix, eventTypes)).must(dateRangeQuery);
    }

    private QueryBuilder getZipCodeQuery(String propertyPrefix, ReportingFilterBean filterBean) {
        return queryHelper.getZipCodeQuery(propertyPrefix, filterBean.getZipCodes());
    }

    private QueryBuilder getCityQuery(String propertyPrefix, ReportingFilterBean filterBean) {
        return queryHelper.getCityQuery(propertyPrefix, filterBean.getCities());
    }

    private QueryBuilder getAgentQuery(String propertyPrefix, ReportingFilterBean filterBean) {
        return queryHelper.getAgentQuery(propertyPrefix, filterBean.getAgents());
    }

}
