package de.immomio.landlord.service.application.customData.mapper.answer;

import de.immomio.data.base.type.application.ApplicationCustomDataAnswerType;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldAnswerBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevelConfig;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApplicationCustomQuestionCustomDataMapperDelegate extends ApplicationCustomDataMapperDelegate {

    private static final String LIST_DELIMITER = ", ";

    private static final String BOOLEAN = "boolean";
    private static final String RANGE_VALUE = "range_value";
    private static final String RANGE_DATE = "range_date";
    private static final String SELECT = "select";
    private static final String MULTISELECT = "multiselect";

    public ApplicationCustomQuestionCustomDataMapperDelegate() {
        super(ApplicationCustomDataFieldType.CUSTOM_QUESTION);
    }

    @PostConstruct
    public void postConstruct() {
        addField(createField(BOOLEAN, ApplicationCustomDataAnswerType.BOOLEAN), this::booleanMapper);
        addField(createField(RANGE_VALUE, ApplicationCustomDataAnswerType.STRING_NUMBER), this::rangeNumberMapper);
        addField(createField(RANGE_DATE, ApplicationCustomDataAnswerType.STRING_DATE), this::rangeDateMapper);
        addField(createField(SELECT, ApplicationCustomDataAnswerType.STRING), this::selectMapper);
        addField(createField(MULTISELECT, ApplicationCustomDataAnswerType.STRING), this::multiselectMapper);
    }

    @Override
    public ApplicationCustomDataFieldBean getFullField(ApplicationCustomDataFieldBaseBean field) {
        ApplicationCustomDataFieldBean baseField = super.getFullField(field);

        return new ApplicationCustomDataFieldBean(baseField.getFieldType(), field.getFieldName(), null, baseField.getCustomQuestionType(),
                field.getCustomQuestionId(), baseField.isAnonymisable(), baseField.getAnswerType(), baseField.getDkLevelCalculation());
    }

    @Override
    protected String getFieldIdentifier(ApplicationCustomDataFieldBaseBean fieldBean) {
        return fieldBean.getCustomQuestionType();
    }

    @Override
    public List<ApplicationCustomDataFieldBaseBean> getPossibleFields(Property property) {
        List<ApplicationCustomDataFieldBaseBean> possibleFields = getPossibleCustomQuestionFieldsForPrioset(property.getPrioset());
        possibleFields.addAll(getPossibleCustomQuestionFieldsForCustomer(property.getCustomer()));
        return possibleFields;
    }

    private static ApplicationCustomDataFieldBean createField(String customQuestionType, ApplicationCustomDataAnswerType answerType) {
        return new ApplicationCustomDataFieldBean(ApplicationCustomDataFieldType.CUSTOM_QUESTION, null, null, customQuestionType, null, false, answerType,
                DkApprovalLevelConfig.DK1);
    }

    private ApplicationCustomDataFieldAnswerBean booleanMapper(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        CustomQuestionResponse response = getCustomQuestionResponse(field.getCustomQuestionId(), application);
        return createAnswer(response != null ? Boolean.valueOf((String) response.getResponse()) : null);
    }

    private ApplicationCustomDataFieldAnswerBean selectMapper(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        CustomQuestionResponse response = getCustomQuestionResponse(field.getCustomQuestionId(), application);
        return createAnswer(response != null ? response.getResponseTitle() : null);
    }

    private ApplicationCustomDataFieldAnswerBean multiselectMapper(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        CustomQuestionResponse response = getCustomQuestionResponse(field.getCustomQuestionId(), application);

        if (response == null) {
            return createAnswer();
        }

        Map<String, String> titleMap = response.getCustomQuestion().getTitleMap();
        String stringAnswer = ((ArrayList<String>) response.getResponse()).stream().map(titleMap::get).collect(Collectors.joining(LIST_DELIMITER));
        return createAnswer(stringAnswer);
    }

    private ApplicationCustomDataFieldAnswerBean rangeNumberMapper(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        CustomQuestionResponse response = getCustomQuestionResponse(field.getCustomQuestionId(), application);

        if (response == null) {
            return createAnswer();
        }

        return ApplicationCustomDataFieldAnswerBean.builder().stringAnswer(response.getResponseTitle()).numberAnswer(response.getSelectedRange()).build();
    }

    private ApplicationCustomDataFieldAnswerBean rangeDateMapper(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        CustomQuestionResponse response = getCustomQuestionResponse(field.getCustomQuestionId(), application);

        if (response == null) {
            return createAnswer();
        }

        Date date = response.getSelectedRange() != null ? new Date(response.getSelectedRange()) : null;
        return ApplicationCustomDataFieldAnswerBean.builder().stringAnswer(response.getResponseTitle()).dateAnswer(date).build();
    }

    private CustomQuestionResponse getCustomQuestionResponse(Long customQuestionId, PropertyApplication application) {
        return application.getUserProfile()
                .getCustomQuestionResponses()
                .stream()
                .filter(response -> response.getCustomQuestion().getId().equals(customQuestionId))
                .findFirst()
                .orElseGet(() -> null);
    }

    private List<ApplicationCustomDataFieldBaseBean> getPossibleCustomQuestionFieldsForPrioset(Prioset prioset) {
        return prioset.getCustomQuestionAssociations()
                .stream()
                .map(PriosetCustomQuestionAssociation::getCustomQuestion)
                .map(this::mapCustomQuestionToField)
                .collect(Collectors.toList());
    }

    private List<ApplicationCustomDataFieldBaseBean> getPossibleCustomQuestionFieldsForCustomer(LandlordCustomer customer) {
        return customer.getGlobalQuestions().stream().map(this::mapCustomQuestionToField).collect(Collectors.toList());
    }

    private ApplicationCustomDataFieldBaseBean mapCustomQuestionToField(CustomQuestion customQuestion) {
        return getField(customQuestion.getQuestionType(), customQuestion.getTitle(), customQuestion.getId());
    }

    private ApplicationCustomDataFieldBean getField(String customQuestionType, String customQuestionName, Long customQuestionId) {
        ApplicationCustomDataFieldBean baseField = fieldMapping.get(customQuestionType);
        return new ApplicationCustomDataFieldBean(baseField.getFieldType(), customQuestionName, null, baseField.getCustomQuestionType(), customQuestionId,
                baseField.isAnonymisable(), baseField.getAnswerType(), baseField.getDkLevelCalculation());
    }
}
