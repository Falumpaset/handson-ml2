package de.immomio.service.impl.tenant;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.config.ApplicationMessageSource;
import de.immomio.constants.GenderType;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionType;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.propertysearcher.entity.user.profile.details.Profession;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureAnswers;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureResponseData;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureSubQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.ChildrenAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonsAnswer;
import de.immomio.model.ApiPropertyInfo;
import de.immomio.model.address.ApiPropertyAddressAnswer;
import de.immomio.model.selfdisclosure.ApiCheckedAnswer;
import de.immomio.model.selfdisclosure.ApiChildAnswer;
import de.immomio.model.selfdisclosure.ApiDocumentAnswer;
import de.immomio.model.selfdisclosure.ApiEmploymentAnswer;
import de.immomio.model.selfdisclosure.ApiPersonAnswer;
import de.immomio.model.selfdisclosure.ApiSdResponseTenant;
import de.immomio.model.selfdisclosure.ApiTenantAddress;
import de.immomio.model.selfdisclosure.SdResponseApiBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class ExternalTenantInfoSdAnswerService {

    private static final String TITLE_SUFFIX = "_TITLE";
    private ApplicationMessageSource messageSource;

    @Value("${answers.select.id.gender}")
    private Long genderAnswerId;

    @Autowired
    public ExternalTenantInfoSdAnswerService(ApplicationMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public SdResponseApiBean parseResponse(PropertyTenant propertyTenant) {
        SdResponseApiBean sdResponseApiBean = new SdResponseApiBean();
        sdResponseApiBean.setId(String.valueOf(propertyTenant.getId()));
        var property = propertyTenant.getProperty();

        populatePropertyData(sdResponseApiBean, property);

        ApiSdResponseTenant tenant = new ApiSdResponseTenant();
        ApiPersonAnswer apiPersonAnswer = new ApiPersonAnswer();

        PropertySearcherUserProfile userProfile = propertyTenant.getUserProfile();
        PropertySearcherUserProfileData profile = userProfile.getData();
        apiPersonAnswer.setFirstName(profile.getFirstname());
        apiPersonAnswer.setLastName(profile.getName());
        apiPersonAnswer.setBirthDate(profile.getDateOfBirth());
        tenant.setPersonDetails(apiPersonAnswer);

        Address userAddress = userProfile.getAddress();
        if (userAddress != null) {
            ApiTenantAddress apiTenantAddress = new ApiTenantAddress(userAddress.getCity(), userAddress.getStreet(), userAddress.getHouseNumber(),
                    userAddress.getZipCode(), userAddress.getRegion(), userProfile.getEmail(), userProfile.getData().getPhone());
            tenant.setAddressDetails(apiTenantAddress);
        }

        Profession profession = profile.getProfession();
        if (profession != null) {
            ApiEmploymentAnswer employmentAnswer = new ApiEmploymentAnswer();
            employmentAnswer.setJob(profession.getSubType());
            employmentAnswer.setNetIncome(profession.getIncome());
            tenant.setEmploymentDetails(employmentAnswer);
        }

        sdResponseApiBean.setTenant(tenant);

        if (profile.getAttachments() != null) {
            List<ApiDocumentAnswer> documentAnswers = profile.getAttachments().stream()
                    .map(s3File -> new ApiDocumentAnswer(s3File.getName(), s3File.getFilename()))
                    .collect(Collectors.toList());
            sdResponseApiBean.setDocuments(documentAnswers);

        }

        return sdResponseApiBean;
    }

    public SdResponseApiBean parseResponse(PropertyTenant propertyTenant, SelfDisclosureResponse selfDisclosureResponse) throws IOException {
        SdResponseApiBean sdResponseApiBean = new SdResponseApiBean();
        sdResponseApiBean.setId(String.valueOf(propertyTenant.getId()));
        var property = propertyTenant.getProperty();

        SelfDisclosureResponseData selfDisclosureResponseData = selfDisclosureResponse.getData();

        List<SelfDisclosureQuestionAnswer> questions = selfDisclosureResponseData.getAnswers().getQuestions();
        populatePropertyData(sdResponseApiBean, property);
        appendDocuments(selfDisclosureResponseData, sdResponseApiBean);
        questions.forEach(questionAnswer -> {
            if (questionAnswer.getAnswer() == null) {
                return;
            }

            switch (questionAnswer.getType()) {
                case PERSON:
                    PersonAnswer personAnswer = parseAnswer(questionAnswer.getAnswer(), PersonAnswer.class);
                    ApiSdResponseTenant tenant = getTenant(personAnswer);
                    fillSubQuestions(tenant, questionAnswer.getSubQuestions());
                    sdResponseApiBean.setTenant(tenant);
                    break;
                case PERSONS:
                    PersonsAnswer[] personAnswers = parseAnswer(questionAnswer.getAnswer(), PersonsAnswer[].class);
                    Arrays.asList(personAnswers).forEach(onePersonAnswer -> {
                        ApiSdResponseTenant createdTenant = getTenant(onePersonAnswer.getAnswer());
                        fillSubQuestions(createdTenant, onePersonAnswer.getSubQuestions());
                        sdResponseApiBean.getFellowTenants().add(createdTenant);
                    });

                    break;

                case CHILDREN:
                    ChildrenAnswer[] childrenAnswers = parseAnswer(questionAnswer.getAnswer(), ChildrenAnswer[].class);
                    Arrays.stream(childrenAnswers)
                            .map(ChildrenAnswer::getAnswer)
                            .forEach(childAnswer -> sdResponseApiBean.getFurtherResidents()
                                    .add(new ApiChildAnswer(childAnswer.getFirstName(), childAnswer.getLastName(), childAnswer.getBirthDate())));
                    break;

                case BOOLEAN:
                    Boolean answer = parseAnswer(questionAnswer.getAnswer(), Boolean.class);
                    ApiCheckedAnswer checkedAnswer = new ApiCheckedAnswer(translate(questionAnswer.getTitle()), answer, null);
                    sdResponseApiBean.getCheckedAnswers().add(checkedAnswer);
                    break;
            }

        });
        selfDisclosureResponseData.getAnswers().getConfirmations().forEach(sdResponseApiBean.getConfirmations()::add);
        return sdResponseApiBean;
    }

    private void appendDocuments(SelfDisclosureResponseData response, SdResponseApiBean apiBean) {
        SelfDisclosureAnswers selfDisclosureAnswers = response.getAnswers();
        List<S3File> uploadedDocuments = selfDisclosureAnswers.getUploadedDocuments();

        List<SelfDisclosureQuestionAnswer> questions = selfDisclosureAnswers.getQuestions();
        List<ApiDocumentAnswer> documents = apiBean.getDocuments();

        uploadedDocuments.forEach(s3File -> {
            documents.add(new ApiDocumentAnswer(s3File.getTitle(), s3File.getFilename()));
        });

        if (questions != null) {
            questions.forEach(selfDisclosureQuestionAnswer -> {
                if (selfDisclosureQuestionAnswer.getUpload() != null) {
                    documents.add(new ApiDocumentAnswer(translate(selfDisclosureQuestionAnswer.getTitle() + TITLE_SUFFIX),
                            selfDisclosureQuestionAnswer.getUpload().getFilename()));
                }

                List<SelfDisclosureSubQuestionAnswer> subQuestions = selfDisclosureQuestionAnswer.getSubQuestions();
                if (subQuestions != null) {
                    subQuestions.stream()
                            .filter(item -> item.getUpload() != null)
                            .forEach(subQuestionAnswer -> documents.add(
                                    new ApiDocumentAnswer(translate(subQuestionAnswer.getTitle() + TITLE_SUFFIX), subQuestionAnswer.getUpload().getFilename())));

                }
            });
        }

    }

    private void populatePropertyData(SdResponseApiBean sdResponseApiBean, Property property) {
        PropertyData propertyData = property.getData();
        Address address = propertyData.getAddress();

        ApiPropertyAddressAnswer apiPropertyAddressAnswer = new ApiPropertyAddressAnswer(address.getCity(), address.getStreet(), address.getHouseNumber(),
                address.getZipCode(), address.getRegion());

        ApiPropertyInfo apiPropertyInfo = ApiPropertyInfo.builder()
                .additionalCosts(propertyData.getServiceCharge())
                .address(apiPropertyAddressAnswer)
                .floor(propertyData.getFloor())
                .heatingCost(propertyData.getHeatingCost())
                .id(property.getExternalId())
                .name(propertyData.getName())
                .rent(propertyData.getBasePrice())
                .rooms(propertyData.getRooms())
                .size(propertyData.getSize())
                .build();

        sdResponseApiBean.setProperty(apiPropertyInfo);
    }

    private ApiSdResponseTenant getTenant(PersonAnswer personAnswer) {
        ApiSdResponseTenant tenant = new ApiSdResponseTenant();

        ApiPersonAnswer apiPersonAnswer = new ApiPersonAnswer();
        apiPersonAnswer.setBirthName(personAnswer.getBirthName());
        apiPersonAnswer.setBirthPlace(personAnswer.getBirthPlace());
        apiPersonAnswer.setFirstName(personAnswer.getFirstName());
        apiPersonAnswer.setLastName(personAnswer.getLastName());
        apiPersonAnswer.setBirthDate(personAnswer.getBirthDate());
        tenant.setPersonDetails(apiPersonAnswer);

        return tenant;
    }

    private void fillSubQuestions(ApiSdResponseTenant tenant, List<SelfDisclosureSubQuestionAnswer> subQuestions) {
        subQuestions.stream()
                .filter(subQuestion -> subQuestion.getType() == SelfDisclosureSubQuestionType.ADDRESS)
                .findFirst()
                .map(subQuestionAnswer -> parseAnswer(subQuestionAnswer.getAnswer(), ApiTenantAddress.class))
                .ifPresent(tenant::setAddressDetails);

        subQuestions.stream()
                .filter(subQuestion -> subQuestion.getType() == SelfDisclosureSubQuestionType.EMPLOYMENT)
                .findFirst()
                .map(subQuestionAnswer -> parseAnswer(subQuestionAnswer.getAnswer(), ApiEmploymentAnswer.class))
                .ifPresent(tenant::setEmploymentDetails);

        subQuestions.stream()
                .filter(subQuestion -> subQuestion.getType() == SelfDisclosureSubQuestionType.BOOLEAN)
                .forEach(subQuestionAnswer -> appendCheckedAnswer(tenant, subQuestionAnswer));

        subQuestions.stream()
                .filter(selfDisclosureSubQuestionAnswer -> selfDisclosureSubQuestionAnswer.getId() != null &&
                        selfDisclosureSubQuestionAnswer.getId().equals(genderAnswerId) &&
                        selfDisclosureSubQuestionAnswer.getAnswer() != null)
                .findFirst()
                .ifPresent(selfDisclosureSubQuestionAnswer -> {
                    try {
                        tenant.getPersonDetails().setGender(GenderType.valueOf(selfDisclosureSubQuestionAnswer.getAnswer().toString()));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }

    private void appendCheckedAnswer(ApiSdResponseTenant tenant, SelfDisclosureSubQuestionAnswer subQuestionAnswer) {
        if (subQuestionAnswer.getAnswer() == null) {
            return;
        }
        Boolean answer = (Boolean) subQuestionAnswer.getAnswer();
        tenant.getCheckedAnswers().add(new ApiCheckedAnswer(translate(subQuestionAnswer.getTitle()), answer, subQuestionAnswer.getComment()));
    }

    private <T> T parseAnswer(Object answer, Class<T> typeOfAnswer) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return objectMapper.convertValue(answer, typeOfAnswer);
    }

    private String translate(String key, String... substitutions) {
        return messageSource.getMessage(key, substitutions, Locale.GERMAN);
    }
}
