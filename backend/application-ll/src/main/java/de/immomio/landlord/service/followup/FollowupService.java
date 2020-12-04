package de.immomio.landlord.service.followup;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.followup.Followup;
import de.immomio.data.landlord.entity.property.followup.FollowupWorkingState;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupBean;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.model.repository.landlord.followup.FollowupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class FollowupService {

    private static final String FOLLOWUP_MUST_NOT_BE_PROCESSED_L = "FOLLOWUP_MUST_NOT_BE_PROCESSED_L";
    private FollowupRepository followupRepository;

    private FollowupNotificationService followupNotificationService;

    private FollowupIntervalSettingsService followupIntervalSettingsService;

    private final PropertyService propertyService;

    @Autowired
    public FollowupService(
            FollowupRepository followupRepository,
            FollowupNotificationService followupNotificationService,
            FollowupIntervalSettingsService followupIntervalSettingsService,
            PropertyService propertyService
    ) {
        this.followupRepository = followupRepository;
        this.followupNotificationService = followupNotificationService;
        this.followupIntervalSettingsService = followupIntervalSettingsService;
        this.propertyService = propertyService;
    }

    public Followup create(Property property, FollowupBean followupBean, Boolean setPropertyReserved) {
        Followup followup = new Followup();
        followup.setProperty(property);
        followup.setState(FollowupWorkingState.UNPROCESSED);
        followup = saveFollowup(followup, followupBean);
        if (setPropertyReserved) {
            propertyService.updatePropertyState(property, PropertyStatus.RESERVED);
        }
        return followup;
    }

    public Followup saveFollowup(Followup followup, FollowupBean followupBean) {
        validateForUpdating(followup);

        followup.setDate(followupBean.getDate());
        followup.setReason(followupBean.getReason());
        followup = followupRepository.save(followup);
        followupNotificationService.updateNotifications(followup, followupBean.getNotifications());

        followupIntervalSettingsService.saveFollowupIntervals(followup.getDate(), followupBean.getNotifications());

        return followup;
    }

    public Followup setProcessed(Followup followup, Boolean processed) {
        followup.setState(processed ? FollowupWorkingState.PROCESSED : FollowupWorkingState.UNPROCESSED);
        return followupRepository.save(followup);
    }

    public void delete(Followup followup) {
        followupRepository.delete(followup);
    }

    private void validateForUpdating(Followup followup) {
        if (followup.getState() == FollowupWorkingState.PROCESSED) {
            throw new ApiValidationException(FOLLOWUP_MUST_NOT_BE_PROCESSED_L);
        }
    }

}
