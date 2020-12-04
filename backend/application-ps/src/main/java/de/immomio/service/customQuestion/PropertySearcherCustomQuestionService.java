package de.immomio.service.customQuestion;

import de.immomio.constants.exceptions.ResponseValidationException;
import de.immomio.data.base.json.JsonModel;
import de.immomio.data.base.json.JsonSchema;
import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseCreateBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseRegisterBean;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.messaging.container.customQuestion.CustomQuestionResponseContainer;
import de.immomio.model.repository.shared.customquestion.CustomQuestionRepository;
import de.immomio.model.repository.shared.customquestion.CustomQuestionResponseRepository;
import de.immomio.model.repository.shared.customquestion.PriosetCustomQuestionAssociationRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class PropertySearcherCustomQuestionService {

    public static final String INVALID_RESPONSE_KEY_NOT_MATCHING_QUESTION_KEYS = "INVALID_RESPONSE_KEY_NOT_MATCHING_QUESTION_KEYS_L";
    public static final String INVALID_RESPONSE_OPTIONS_CONTAINED = "INVALID_RESPONSE_OPTIONS_CONTAINED_L";
    public static final String EMPTY_RESPONSE_MODEL = "EMPTY_RESPONSE_MODEL_L";
    private static final String INTERNAL_SERVER_ERROR_NO_QUESTION_TO_MATCH_AGAINST = "INTERNAL_SERVER_ERROR_NO_QUESTION_TO_MATCH_AGAINST_L";

    private final CustomQuestionResponseRepository responseRepository;

    private final PriosetCustomQuestionAssociationRepository associationRepository;

    private final CustomQuestionRepository customQuestionRepository;

    private final PropertySearcherCustomQuestionResponseMessageSender messageSender;

    private final CustomQuestionConverter customQuestionConverter;

    private final PropertyRepository propertyRepository;

    @Autowired
    public PropertySearcherCustomQuestionService(CustomQuestionResponseRepository responseRepository,
            PriosetCustomQuestionAssociationRepository associationRepository,
            CustomQuestionRepository customQuestionRepository,
            PropertySearcherCustomQuestionResponseMessageSender messageSender,
            CustomQuestionConverter customQuestionConverter, PropertyRepository propertyRepository) {
        this.responseRepository = responseRepository;
        this.associationRepository = associationRepository;
        this.customQuestionRepository = customQuestionRepository;
        this.messageSender = messageSender;
        this.customQuestionConverter = customQuestionConverter;
        this.propertyRepository = propertyRepository;
    }

    public void respondToAll(List<CustomQuestionResponseRegisterBean> customQuestionResponses,
            PropertySearcherUserProfile userProfile) {
        if (customQuestionResponses != null) {
            Map<Long, CustomQuestionResponseRegisterBean> customQuestionsIds = getMapFromResponseList(customQuestionResponses);

            customQuestionRepository.findAllById(customQuestionsIds.keySet()).forEach(customQuestion -> {
                try {
                    respond(customQuestionsIds.get(customQuestion.getId()), userProfile, customQuestion, false);
                } catch (ResponseValidationException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    public void respondToGlobals(List<CustomQuestionResponseRegisterBean> customQuestionResponses,
            PropertySearcherUserProfile userProfile) {
        if (userProfile.getTenantPoolCustomer() != null && customQuestionResponses != null) {
            Map<Long, CustomQuestionResponseRegisterBean> customQuestionsIds = getMapFromResponseList(customQuestionResponses);

            customQuestionRepository.findAllById(customQuestionsIds.keySet())
                    .stream()
                    .filter(customQuestion -> customQuestion.getType() == CustomQuestionType.GLOBAL)
                    .forEach(customQuestion -> {
                        try {
                            respond(customQuestionsIds.get(customQuestion.getId()), userProfile, customQuestion, false);
                        } catch (ResponseValidationException e) {
                            log.error(e.getMessage(), e);
                        }
                    });
        }
    }

    public List<CustomQuestionBean> findForProperty(Long propertyId) {
        Property property = propertyRepository.customFindById(propertyId).orElseThrow();
        List<PriosetCustomQuestionAssociation> associations = associationRepository.findByPrioset(
                property.getPrioset());

        List<CustomQuestionBean> customQuestionBeans = associations.stream()
                .map(PriosetCustomQuestionAssociation::getCustomQuestion)
                .map(customQuestionConverter::getCustomQuestionBean)
                .collect(Collectors.toList());

        Page<CustomQuestion> globalQuestions = customQuestionRepository.findByCustomerAndType(property.getCustomer(),
                CustomQuestionType.GLOBAL, PageRequest.of(0, Integer.MAX_VALUE));
        customQuestionBeans.addAll(
                globalQuestions.getContent().stream().map(customQuestionConverter::getCustomQuestionBean).collect(Collectors.toList()));

        return customQuestionBeans;
    }

    public CustomQuestionResponse respond(CustomQuestionResponseCreateBean response, PropertySearcherUserProfile userProfile,
                                          CustomQuestion question, boolean sendMessage) throws ResponseValidationException {
        JsonModel data = response.getData();
        validateResponse(question.getSchema(), data);

        Optional<CustomQuestionResponse> existingResponse = responseRepository.findByUserProfileAndCustomQuestion(userProfile,
                question);

        CustomQuestionResponse customQuestionResponse = existingResponse.orElse(
                new CustomQuestionResponse(userProfile, question));

        customQuestionResponse.setData(data);
        customQuestionResponse.setComment(response.getComment());
        customQuestionResponse.setSelectedRange(response.getSelectedRange());

        CustomQuestionResponse savedResponse = save(customQuestionResponse);

        if (sendMessage) {
            messageSender.sendCustomQuestionResponseMessage(new CustomQuestionResponseContainer(savedResponse.getId()));
        }
        return savedResponse;
    }

    public Optional<CustomQuestion> findById(Long id) {
        return customQuestionRepository.findById(id);
    }

    public CustomQuestionResponse save(CustomQuestionResponse customQuestionResponse) {
        return responseRepository.save(customQuestionResponse);
    }

    public List<CustomQuestionBean> findForUserProfileAndProperty(PropertySearcherUserProfile userProfile, Property property) {
        List<PriosetCustomQuestionAssociation> associations = associationRepository.findByPrioset(
                property.getPrioset());

        List<CustomQuestionBean> customQuestionBeans = associations.stream()
                .map(PriosetCustomQuestionAssociation::getCustomQuestion)
                .map(getCustomQuestionBean(userProfile))
                .collect(Collectors.toList());

        Page<CustomQuestion> globalQuestions = customQuestionRepository.findByCustomerAndType(property.getCustomer(),
                CustomQuestionType.GLOBAL, PageRequest.of(0, Integer.MAX_VALUE));
        customQuestionBeans.addAll(
                globalQuestions.getContent().stream().map(getCustomQuestionBean(userProfile)).collect(Collectors.toList()));

        return customQuestionBeans;
    }

    private Map<Long, CustomQuestionResponseRegisterBean> getMapFromResponseList(List<CustomQuestionResponseRegisterBean> customQuestionResponses) {
        return customQuestionResponses.stream()
                .filter(customQuestionResponseRegisterBean -> customQuestionResponseRegisterBean.getCustomQuestionId()
                        != null)
                .collect(Collectors.toMap(CustomQuestionResponseRegisterBean::getCustomQuestionId,
                        Function.identity()));
    }

    private void validateResponse(JsonSchema question, JsonModel response) throws ResponseValidationException {
        if (response.isEmpty()) {
            throw new ResponseValidationException(EMPTY_RESPONSE_MODEL);
        }

        responseIsValid(question, response);
    }

    private void responseIsValid(JsonSchema question, JsonModel response) throws ResponseValidationException {
        Map<String, JsonSchema> properties = question.getProperties();

        if (properties == null) {
            throw new ResponseValidationException(INTERNAL_SERVER_ERROR_NO_QUESTION_TO_MATCH_AGAINST);
        }

        Set<String> questionKeys = properties.keySet();
        if (!questionKeys.containsAll(response.keySet())) {
            throw new ResponseValidationException(INVALID_RESPONSE_KEY_NOT_MATCHING_QUESTION_KEYS);
        }

        for (String key : questionKeys) {
            List<Object> responsesValues;
            try {
                responsesValues = (List<Object>) response.get(key);
            } catch (ClassCastException e) {
                responsesValues = Arrays.asList(response.get(key));
            }

            if (!properties.get(key).getEnums().containsAll(responsesValues)) {
                throw new ResponseValidationException(INVALID_RESPONSE_OPTIONS_CONTAINED);
            }
        }
    }

    private Function<CustomQuestion, CustomQuestionBean> getCustomQuestionBean(PropertySearcherUserProfile userProfile) {
        return customQuestion -> {
            List<CustomQuestionResponse> responses = responseRepository.findAllByUserProfileAndCustomQuestion(userProfile,
                    Collections.singletonList(customQuestion));
            CustomQuestionBean customQuestionBean = customQuestionConverter.getCustomQuestionBean(customQuestion);
            customQuestionBean.setResponses(responses.stream()
                    .map(customQuestionConverter::getCustomQuestionResponseBean)
                    .collect(Collectors.toList()));
            return customQuestionBean;
        };
    }
}
