package de.immomio.landlord.service.reporting.data;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.service.reporting.BaseElasticsearchDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class BaseLandlordDataHandler extends BaseElasticsearchDataHandler {

    protected UserSecurityService userSecurityService;

    public BaseLandlordDataHandler(UserSecurityService userSecurityService, RestHighLevelClient client) {
        super(client);
        this.userSecurityService = userSecurityService;
    }

    protected Long getCustomerId() {
        return getPrincipalUser().getCustomer().getId();
    }

    protected LandlordUser getPrincipalUser() {
        return userSecurityService.getPrincipalUser();
    }



}
