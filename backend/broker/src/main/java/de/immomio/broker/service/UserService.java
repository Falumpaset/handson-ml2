package de.immomio.broker.service;

import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.application.BasePropertyApplicationRepository;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */

@Service
public class UserService {

    private final BasePropertyApplicationRepository applicationRepository;

    @Value("${proposal.createThresholdWeeks}")
    private Integer proposalThreshold;

    @Autowired
    public UserService(BasePropertyApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public boolean isUserBlockedForCreatingProposals(PropertySearcherUserProfile userProfile) {
        if (userProfile.getCreated().before(LocalDate.now().minusWeeks(proposalThreshold).toDate())) {
            return false;
        }

        Optional<PropertyApplication> firstApplication = applicationRepository.findFirstByUserProfileOrderByCreatedAsc(userProfile);
        if (firstApplication.isPresent()) {
            PropertyApplication application = firstApplication.get();
            Portal portal = application.getPortal();
            Date created = application.getCreated();
            Date enablingThreshold = LocalDateTime.now().minusWeeks(proposalThreshold).toDate();

            return portal != null && created.after(enablingThreshold) && application.getStatus() != ApplicationStatus.REJECTED;
        }

        return false;
    }
}
