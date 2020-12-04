package de.immomio.landlord.service.application.customData;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBundle;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataEmailRequestBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataRequestBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.application.customData.mapper.answer.LandlordPropertyApplicationCustomDataAnswerMapperService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.user.settings.LandlordUserSettingsService;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.utils.EmailAddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class LandlordPropertyApplicationCustomDataService {

    private static final String INVALID_EMAIL_ADDRESS_L = "INVALID_EMAIL_ADDRESS_L";
    private static final String PROPERTY_NOT_FOUND_L = "PROPERTY_NOT_FOUND_L";

    private final LandlordPropertyApplicationCustomDataAnswerMapperService answerMapperService;
    private final LandlordPropertyApplicationCustomDataReportService reportService;
    private final PropertyApplicationRepository applicationRepository;
    private final LandlordUserSettingsService userSettingsService;
    private final LandlordPropertyApplicationCustomDataNotificationService notificationService;
    private final UserSecurityService securityService;
    private final PropertyRepository propertyRepository;

    @Autowired
    public LandlordPropertyApplicationCustomDataService(LandlordPropertyApplicationCustomDataAnswerMapperService answerMapperService,
            LandlordPropertyApplicationCustomDataReportService reportService,
            PropertyApplicationRepository applicationRepository,
            LandlordUserSettingsService userSettingsService,
            LandlordPropertyApplicationCustomDataNotificationService notificationService,
            UserSecurityService securityService,
            PropertyRepository propertyRepository) {
        this.answerMapperService = answerMapperService;
        this.reportService = reportService;
        this.applicationRepository = applicationRepository;
        this.userSettingsService = userSettingsService;
        this.notificationService = notificationService;
        this.securityService = securityService;
        this.propertyRepository = propertyRepository;
    }

    public File createDatafile(ApplicationCustomDataRequestBean requestBean) {
        LandlordUser user = securityService.getPrincipalUser();
        Property property = getProperty(requestBean.getPropertyId(), user.getCustomer());

        ApplicationCustomDataBundle customDataBundle = mapApplicationsToFields(requestBean, property);
        return reportService.getCustomDataAsXlsxFile(customDataBundle, property);
    }

    public void sendDataFile(ApplicationCustomDataEmailRequestBean requestBean) {
        if (requestBean.getRecipients().stream().anyMatch(EmailAddressUtils::isInvalid)) {
            throw new ApiValidationException(INVALID_EMAIL_ADDRESS_L);
        }

        LandlordUser user = securityService.getPrincipalUser();
        Property property = getProperty(requestBean.getPropertyId(), user.getCustomer());

        ApplicationCustomDataBundle customDataBundle = mapApplicationsToFields(requestBean, property);
        requestBean.getRecipients().forEach(recipient -> {
            File file = reportService.getCustomDataAsXlsxFile(customDataBundle, property);
            notificationService.sendCustomDataFile(user, property, recipient, file);
        });
    }

    public ApplicationCustomDataBundle mapApplicationsToFields(ApplicationCustomDataRequestBean requestBean) {
        LandlordUser user = securityService.getPrincipalUser();
        Property property = getProperty(requestBean.getPropertyId(), user.getCustomer());
        return mapApplicationsToFields(requestBean, property);
    }

    public ApplicationCustomDataBundle mapApplicationsToFields(ApplicationCustomDataRequestBean requestBean, Property property) {
        List<PropertyApplication> applications = applicationRepository.findAllByIdInAndPropertyOrderByScoreDesc(requestBean.getApplicationIds(), property);

        List<ApplicationCustomDataFieldBaseBean> fields = requestBean.getFields();
        boolean anonymised = requestBean.isAnonymised();

        userSettingsService.saveCustomDataModalSettings(fields, anonymised);

        return answerMapperService.mapApplicationsToFields(fields, applications, anonymised);
    }

    public List<ApplicationCustomDataFieldBaseBean> getPossibleFields(Property property) {
        return answerMapperService.getPossibleFields(property);
    }

    private Property getProperty(Long propertyId, LandlordCustomer customer) {
        return propertyRepository.findByIdAndCustomer(propertyId, customer).orElseThrow(() -> new ApiValidationException(PROPERTY_NOT_FOUND_L));
    }
}
