package de.immomio.landlord.service.application.customData.mapper.answer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldAnswerBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.ChildrenAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonsAnswer;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.immomio.data.base.type.application.ApplicationCustomDataAnswerType.NUMBER;
import static de.immomio.data.landlord.bean.property.dk.DkApprovalLevelConfig.DK2;

@Component
public class ApplicationSelfDisclosureDataMapperDelegate extends ApplicationCustomDataMapperDelegate {

    private static final String SELF_DISCLOSURE_CHILDREN_ADULT_L = "SELF_DISCLOSURE_CHILDREN_ADULT_L";
    private static final String SELF_DISCLOSURE_CHILDREN_MINOR_L = "SELF_DISCLOSURE_CHILDREN_MINOR_L";

    private final SelfDisclosureResponseRepository selfDisclosureResponseRepository;

    @Autowired
    public ApplicationSelfDisclosureDataMapperDelegate(SelfDisclosureResponseRepository selfDisclosureResponseRepository) {
        super(ApplicationCustomDataFieldType.SELF_DISCLOSURE);
        this.selfDisclosureResponseRepository = selfDisclosureResponseRepository;
    }

    @PostConstruct
    public void postConstruct() {
        addField(createField("persons"), this::personsCount);
        addField(createField("children"), this::childrenCount);
    }

    @Override
    public List<ApplicationCustomDataFieldBaseBean> getPossibleFields(Property property) {
        return property.getCustomer().isSelfDisclosureAllowed() ? new ArrayList<>(mapperDelegates.keySet()) : List.of();
    }

    private ApplicationCustomDataFieldBean createField(String fieldType) {
        return new ApplicationCustomDataFieldBean(getType(), CUSTOM_DATA + fieldType, fieldType, null, null, false, NUMBER, DK2);
    }

    private ApplicationCustomDataFieldAnswerBean personsCount(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        Optional<SelfDisclosureQuestionAnswer> optionalSelfDisclosureQuestionAnswer = getAnswerByQuestionTitel(application, SELF_DISCLOSURE_CHILDREN_ADULT_L);

        Integer personsCount = null;
        if (optionalSelfDisclosureQuestionAnswer.isPresent()) {
            personsCount = parseAnswer(optionalSelfDisclosureQuestionAnswer.get().getAnswer(), PersonsAnswer[].class).length + 1;
        }

        return createAnswer(personsCount);
    }

    private ApplicationCustomDataFieldAnswerBean childrenCount(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        Optional<SelfDisclosureQuestionAnswer> optionalSelfDisclosureQuestionAnswer = getAnswerByQuestionTitel(application, SELF_DISCLOSURE_CHILDREN_MINOR_L);

        Integer childrenCount = null;
        if (optionalSelfDisclosureQuestionAnswer.isPresent()) {
            childrenCount = parseAnswer(optionalSelfDisclosureQuestionAnswer.get().getAnswer(), ChildrenAnswer[].class).length;
        }

        return createAnswer(childrenCount);
    }

    private Optional<SelfDisclosureQuestionAnswer> getAnswerByQuestionTitel(PropertyApplication application, String questionId) {
        return selfDisclosureResponseRepository.findFirstByPropertyAndUserProfile(application.getProperty(), application.getUserProfile())
                .flatMap(disclosureResponse -> disclosureResponse.getData()
                        .getAnswers()
                        .getQuestions()
                        .stream()
                        .filter(selfDisclosureQuestionAnswer -> selfDisclosureQuestionAnswer.getTitle().equals(questionId))
                        .findFirst());
    }

    private <T> T parseAnswer(Object answer, Class<T> typeOfAnswer) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(answer, typeOfAnswer);
    }
}
