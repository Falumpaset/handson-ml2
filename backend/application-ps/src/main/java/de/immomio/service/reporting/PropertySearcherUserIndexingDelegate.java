package de.immomio.service.reporting;

import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class PropertySearcherUserIndexingDelegate extends BasePropertySearcherIndexingDelegate {

    @Autowired
    public PropertySearcherUserIndexingDelegate(
            UserSecurityService userSecurityService,
            PropertySearcherIndexingSenderService propertySearcherIndexingService
    ) {
        super(userSecurityService, propertySearcherIndexingService);
    }

    public void proposalDenied(PropertyProposal proposal) {
        propertySearcherIndexingService.proposalDenied(proposal, getPrincipal().getMainProfile());
    }

    public void proposalAccepted(PropertyProposal proposal) {
        propertySearcherIndexingService.proposalAccepted(proposal, getPrincipal().getMainProfile());
    }

}
