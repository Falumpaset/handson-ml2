package de.immomio.service.indexing;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertySearcherProfileChangedIndexingService {

    private PropertyApplicationRepository applicationRepository;
    private PropertySearcherApplicationIndexingDelegate indexingDelegate;

    @Autowired
    public PropertySearcherProfileChangedIndexingService(PropertyApplicationRepository applicationRepository,
            PropertySearcherApplicationIndexingDelegate indexingDelegate) {
        this.applicationRepository = applicationRepository;
        this.indexingDelegate = indexingDelegate;
    }


    public void indexProfileChanged(Long userProfileId) {
        List<PropertyApplication> applications = applicationRepository.findByUserProfileId(userProfileId);

        applications.forEach(indexingDelegate::profileChanged);
    }
}
