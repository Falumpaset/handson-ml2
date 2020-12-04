package de.immomio.service.application;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ApplicationSaveException;
import de.immomio.data.base.type.application.ApplicationStatus;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.property.dk.DkApproval;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DkApprovalService {

    private static final String IS_MIN_LEVEL_DK = "IS_MIN_LEVEL_DK_L";
    private static final String IS_MAX_LEVEL_DK = "IS_MAX_LEVEL_DK_L";
    private static final String DK_LEVEL_UPDATE_FOR_INTENT_FAILED = "DK_LEVEL_UPDATE_FOR_INTENT_FAILED_L";
    private static final String INTENT_MISSING_FOR_DK_CHANGE_L = "INTENT_MISSING_FOR_DK_CHANGE_L";

    private final PropertyApplicationRepository propertyApplicationRepository;

    @Autowired
    public DkApprovalService(PropertyApplicationRepository propertyApplicationRepository) {
        this.propertyApplicationRepository = propertyApplicationRepository;
    }

    public PropertyApplication reset(PropertyApplication application) throws ApplicationSaveException {
        application.getDkApprovals().add(new DkApproval(application, DkApprovalLevel.DK1));
        try {
            return propertyApplicationRepository.save(application);
        } catch (Exception e) {
            throw new ApplicationSaveException(e.getMessage(), e);
        }
    }

    public PropertyApplication increase(PropertyApplication application) throws ApplicationSaveException {
        DkApprovalLevel nextApprovalLevel = application.getDkApprovalLevel().nextDkLevel();
        return alterDKLevel(application, nextApprovalLevel, IS_MAX_LEVEL_DK);
    }

    public PropertyApplication decrease(PropertyApplication application) throws ApplicationSaveException {
        DkApprovalLevel prevApprovalLevel = application.getDkApprovalLevel().prevDkLevel();
        return alterDKLevel(application, prevApprovalLevel, IS_MIN_LEVEL_DK);
    }

    private PropertyApplication alterDKLevel(PropertyApplication application, DkApprovalLevel levelToAlterTo, String message)
            throws ApplicationSaveException {
        if (levelToAlterTo == null) {
            throw new ApiValidationException(message);
        }
//        TODO if we want to reinstate the DK3 level limit we need to do it here:
//        if (levelToAlterTo == DkApprovalLevel.DK3) {
//            Long approved = countApprovedByPropertyAndLevel(application.getProperty(), DkApprovalLevel.DK3);
//            if (approved >= MAX_DK3_APPROVALS_PER_PROPERTY) {
//                throw new ApiValidationException(DK3_LIMIT_REACHED);
//            }
//        }

        boolean isIntentNeededButMissing = levelToAlterTo == DkApprovalLevel.DK2 &&
                application.getProperty().getCustomer().getCustomerSettings().getDkLevelCustomerSettings().getIntentNeededBeforeDk2() &&
                application.getStatus() != ApplicationStatus.INTENT;
        if (isIntentNeededButMissing) {
            throw new ApiValidationException(INTENT_MISSING_FOR_DK_CHANGE_L);
        }

        application.getDkApprovals().add(new DkApproval(application, levelToAlterTo));
        try {
            return propertyApplicationRepository.save(application);
        } catch (Exception e) {
            throw new ApplicationSaveException(e.getMessage(), e);
        }
    }

}
