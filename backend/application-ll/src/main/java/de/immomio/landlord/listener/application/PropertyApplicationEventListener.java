package de.immomio.landlord.listener.application;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.property.notification.PropertyApplicationNotificationService;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.property.cache.PropertyCountRefreshCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(PropertyApplication.class)
public class PropertyApplicationEventListener {

    private final PropertyApplicationNotificationService propertyApplicationNotificationService;

    private final PropertyApplicationRepository propertyApplicationRepository;

    private final PropertyCountRefreshCacheService refreshPropertyCountCache;

    @Autowired
    public PropertyApplicationEventListener(
            PropertyApplicationNotificationService propertyApplicationNotificationService,
            PropertyApplicationRepository propertyApplicationRepository,
            PropertyCountRefreshCacheService refreshPropertyCountCache
    ) {
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
        this.propertyApplicationRepository = propertyApplicationRepository;
        this.refreshPropertyCountCache = refreshPropertyCountCache;
    }

    @HandleAfterDelete
    public void handleAfterDelete(PropertyApplication application) {
        refreshPropertyCountCache.refreshApplicationCache(application.getProperty());
    }
}
