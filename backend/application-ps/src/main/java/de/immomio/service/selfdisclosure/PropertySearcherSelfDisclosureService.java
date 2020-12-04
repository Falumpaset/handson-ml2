package de.immomio.service.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureResponseBean;
import de.immomio.constants.exceptions.ApiNotFoundException;
import de.immomio.constants.exceptions.ResponseForbiddenException;
import de.immomio.constants.exceptions.ResponseValidationException;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureResponseData;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureRepository;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureResponseRepository;
import de.immomio.service.landlord.selfdisclosure.AbstractSelfDisclosureService;
import de.immomio.service.landlord.selfdisclosure.SelfDisclosureDataConverter;
import de.immomio.service.reporting.PropertySearcherApplicationIndexingDelegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PropertySearcherSelfDisclosureService extends AbstractSelfDisclosureService<SelfDisclosureRepository> {

    private static final String EMPTY_RESPONSE_MODEL = "EMPTY_RESPONSE_MODEL_L";

    private static final String SELF_DISCLOSURE_RESPONSE_PROCESS_NOT_ALLOWED =
            "SELF_DISCLOSURE_RESPONSE_PROCESS_NOT_ALLOWED_L";

    private final SelfDisclosureResponseRepository selfDisclosureResponseRepository;

    private final PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate;

    private final PropertyApplicationRepository applicationRepository;

    private final SelfDisclosureDataConverter selfDisclosureDataConverter;

    @Autowired
    public PropertySearcherSelfDisclosureService(
            SelfDisclosureResponseRepository selfDisclosureResponseRepository,
            SelfDisclosureRepository selfDisclosureRepository,
            PropertySearcherApplicationIndexingDelegate applicationIndexingDelegate,
            PropertyApplicationRepository applicationRepository,
            SelfDisclosureDataConverter selfDisclosureDataConverter) {
        super(selfDisclosureRepository);
        this.selfDisclosureResponseRepository = selfDisclosureResponseRepository;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
        this.applicationRepository = applicationRepository;
        this.selfDisclosureDataConverter = selfDisclosureDataConverter;
    }

    public boolean allowSelfDisclosureProcess(Property property) {
        return property.getCustomer().isSelfDisclosureAllowed() && property.isShowSelfDisclosureQuestions();
    }

    public SelfDisclosureResponse saveOrUpdateResponse(
            SelfDisclosureResponseBean responseBean,
            Property property,
            PropertySearcherUserProfile user
    ) throws ResponseValidationException, ResponseForbiddenException {
        if (!allowSelfDisclosureProcess(property)) {
            throw new ResponseForbiddenException(SELF_DISCLOSURE_RESPONSE_PROCESS_NOT_ALLOWED);
        }

        SelfDisclosure selfDisclosure = property.getCustomer().getSelfDisclosure();
        SelfDisclosureResponseData response = responseBean.getData();
        validateResponse(responseBean.getData());

        Optional<SelfDisclosureResponse> existingResponse = selfDisclosureResponseRepository.findByPropertyIdAndUserProfileId(
                property.getId(),
                user.getId()
        );

        SelfDisclosureResponse selfDisclosureResponse = existingResponse.orElse(
                new SelfDisclosureResponse(user, property, selfDisclosure)
        );

        selfDisclosureResponse.setData(response);
        boolean isNew = selfDisclosureResponse.isNew();
        SelfDisclosureResponse saved = selfDisclosureResponseRepository.save(selfDisclosureResponse);
        indexSelfDisclosureResponse(property, user, isNew);
        return saved;
    }

    public SelfDisclosureResponseBean findResponse(Property property, PropertySearcherUserProfile userProfile) {
        return selfDisclosureResponseRepository.findByPropertyIdAndUserProfileId(property.getId(), userProfile.getId())
                .map(selfDisclosureDataConverter::selfDisclosureResponseToBean)
                .orElseThrow(ApiNotFoundException::new);
    }

    private void indexSelfDisclosureResponse(Property property, PropertySearcherUserProfile userProfile, boolean isNew) {
        PropertyApplication application = applicationRepository.findByUserProfileIdAndPropertyId(userProfile.getId(), property.getId());
        if (application != null) {
            if (isNew) {
                applicationIndexingDelegate.selfDisclosureCreated(application);
            } else {
                applicationIndexingDelegate.selfDisclosureUpdated(application);
            }
        }
    }

    private void validateResponse(SelfDisclosureResponseData response) throws ResponseValidationException {
        if (response == null || response.getAnswers() == null) {
            throw new ResponseValidationException(EMPTY_RESPONSE_MODEL);
        }
    }

}
