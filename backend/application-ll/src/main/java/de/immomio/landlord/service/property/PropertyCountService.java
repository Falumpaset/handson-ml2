package de.immomio.landlord.service.property;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.service.property.cache.PropertyCacheCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class PropertyCountService {

    private PropertyCacheCountService propertyCacheCountService;


    private PropertyApplicationRepository applicationRepository;

    @Autowired
    public PropertyCountService(
            PropertyCacheCountService propertyCacheCountService,
            PropertyApplicationRepository applicationRepository
    ) {
        this.propertyCacheCountService = propertyCacheCountService;
        this.applicationRepository = applicationRepository;
    }

    public void populatePropertiesWithCountData(List<Property> properties) {
        if (!properties.isEmpty()) {
            populateProposalCount(properties);
            populateApplicationCount(properties);
            populateInviteeCount(properties);
            populateNewApplicationCount(properties);
        }
    }

    public void populatePropertyWithCountData(Property property) {
        List<Property> propertyList = Collections.singletonList(property);
        populatePropertiesWithCountData(propertyList);
    }

    public Long getApplicationCount(Long propertyId) {
        return propertyCacheCountService.getApplicationCount(propertyId);
    }

    private void populateProposalCount(List<Property> properties) {
        properties.forEach(property -> property.setSizeOfProposals(propertyCacheCountService.getProposalCount(property.getId())));
    }

    private void populateApplicationCount(List<Property> properties) {
        properties.forEach(property -> property.setSizeOfApplications(propertyCacheCountService.getApplicationCount(property.getId())));
    }

    private void populateInviteeCount(List<Property> properties) {
        properties.forEach(property -> property.setSizeOfInvitees(propertyCacheCountService.getInviteeCount(property.getId())));
    }

    private void populateNewApplicationCount(List<Property> properties) {
        properties.forEach(property -> {
            Date applicationsViewed = property.getApplicationsViewed();
            if(applicationsViewed == null) {
                applicationsViewed = new Date();
            }
            property.setNewApplications(applicationRepository.getCountUnseenApplications(property.getId(), applicationsViewed));
        });
    }

}
