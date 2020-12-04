package de.immomio.landlord.service.reporting.query;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.landlord.service.security.UserSecurityService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Component
public class AggregationQueryHelper {

    private final UserSecurityService userSecurityService;

    @Autowired
    public AggregationQueryHelper(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    public QueryBuilder getAgentQuery(String propertyPrefix, List<Long> agents) {
        LandlordUser principalUser = getPrincipalUser();
        QueryBuilder editorQuery = QueryBuilders.matchAllQuery();

        String principalField = propertyPrefix == null ? "property.agentInfo.id" : propertyPrefix + ".property.agentInfo.id";
        if (principalUser.getUsertype() == LandlordUsertype.COMPANYADMIN) {
            if (agents != null && !agents.isEmpty()) {
                BoolQueryBuilder agentQuery = new BoolQueryBuilder().minimumShouldMatch(1);
                agents.forEach(agentId -> agentQuery.should(QueryBuilders.matchQuery(principalField, agentId)));
                return agentQuery;
            }
        } else {
            editorQuery = QueryBuilders.matchQuery(principalField, principalUser.getId());
        }

        return editorQuery;
    }

    public QueryBuilder getCityQuery(String propertyPrefix, List<String> cities) {
        String cityField = propertyPrefix == null ? "property.data.address.city" : propertyPrefix + ".property.data.address.city";

        if (cities == null || cities.isEmpty()) {
            return QueryBuilders.matchAllQuery();
        }

        BoolQueryBuilder cityQuery = new BoolQueryBuilder().minimumShouldMatch(1);
        if (!cities.isEmpty()) {
            cities.forEach(city -> cityQuery.should(QueryBuilders.matchQuery(cityField, city)));
        }

        return cityQuery;
    }

    public QueryBuilder getZipCodeQuery(String propertyPrefix, List<String> zipCodes) {
        String zipCodeField = propertyPrefix == null ? "property.data.address.zipCode" : propertyPrefix + ".property.data.address.zipCode";

        if (zipCodes == null || zipCodes.isEmpty()) {
            return QueryBuilders.matchAllQuery();
        }

        BoolQueryBuilder zipCodeQuery = new BoolQueryBuilder().minimumShouldMatch(1);

        if (!zipCodes.isEmpty()) {
            zipCodes.forEach(city -> zipCodeQuery.should(QueryBuilders.matchQuery(zipCodeField, city)));
        }

        return zipCodeQuery;
    }

    private LandlordUser getPrincipalUser() {
        return userSecurityService.getPrincipalUser();
    }
}
