package de.immomio.controller.analytics;

import de.immomio.landlord.analytics.BasePropertyApplicationAnalyticsController;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.property.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestController
@RequestMapping(value = "/applications/analytics")
public class PropertyApplicationAnalyticsController extends BasePropertyApplicationAnalyticsController {

    @Autowired
    public PropertyApplicationAnalyticsController(
            PropertyRepository propertyRepository,
            UserSecurityService userSecurityService
    ) {
        this.propertyRepository = propertyRepository;
        this.userSecurityService = userSecurityService;
    }

}
