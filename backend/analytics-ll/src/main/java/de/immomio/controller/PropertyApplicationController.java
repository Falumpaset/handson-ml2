package de.immomio.controller;

import de.immomio.landlord.analytics.BasePropertyApplicationAnalyticsController;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.property.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Maik Kingma
 */

@RepositoryRestController
@RequestMapping(value = "/applications")
public class PropertyApplicationController extends BasePropertyApplicationAnalyticsController {

    @Autowired
    public PropertyApplicationController(
            PropertyRepository propertyRepository,
            UserSecurityService userSecurityService
    ) {
        this.propertyRepository = propertyRepository;
        this.userSecurityService = userSecurityService;
    }

}
