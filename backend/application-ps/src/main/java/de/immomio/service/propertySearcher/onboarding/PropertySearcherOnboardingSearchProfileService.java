package de.immomio.service.propertySearcher.onboarding;

import de.immomio.data.propertysearcher.bean.searchprofile.SearchProfileBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.service.searchProfile.SearchProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertySearcherOnboardingSearchProfileService {

    private final SearchProfileService searchProfileService;

    @Autowired
    public PropertySearcherOnboardingSearchProfileService(SearchProfileService searchProfileService) {
        this.searchProfileService = searchProfileService;
    }

    public void createSearchProfileForUserIfNecessary(PropertySearcherUserProfile userProfile, SearchProfileBean searchProfile) {
        if (userProfile.getUser().getProspectOptIn() != null && userProfile.getUser().getProspectOptIn().isOptInForProspect()) {

            createSearchProfilesForApplications(userProfile);

            if (searchProfile != null && searchProfileService.searchProfileComplete(searchProfile)) {
                try {
                    searchProfileService.create(searchProfile, userProfile);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void createSearchProfilesForApplications(PropertySearcherUserProfile userProfile) {
        userProfile.getApplications().forEach(propertyApplication -> searchProfileService.create(propertyApplication.getProperty(), userProfile));
    }
}