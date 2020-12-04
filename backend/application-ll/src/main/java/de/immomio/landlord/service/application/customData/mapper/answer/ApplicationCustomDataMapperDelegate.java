package de.immomio.landlord.service.application.customData.mapper.answer;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldAnswerBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.util.PhraseAppKeyUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class ApplicationCustomDataMapperDelegate {

    protected static final String CUSTOM_DATA = "CustomData.";
    private static final String FIELD_NOT_FOUND_L = "FIELD_NOT_FOUND_L";

    protected final Map<ApplicationCustomDataFieldBean, BiFunction<ApplicationCustomDataFieldBean, PropertyApplication, ApplicationCustomDataFieldAnswerBean>> mapperDelegates = new LinkedHashMap<>();
    protected final Map<String, ApplicationCustomDataFieldBean> fieldMapping = new LinkedHashMap<>();

    private final ApplicationCustomDataFieldType type;

    public ApplicationCustomDataMapperDelegate(ApplicationCustomDataFieldType type) {
        this.type = type;
    }

    public ApplicationCustomDataFieldType getType() {
        return type;
    }

    public ApplicationCustomDataFieldAnswerBean apply(ApplicationCustomDataFieldBean field, PropertyApplication application) {
        if (!mapperDelegates.containsKey(field)) {
            throw new ApiValidationException(FIELD_NOT_FOUND_L);
        }

        if (field.getDkApprovalLevel(application.getProperty().getCustomer().getCustomerSettings().getDkLevelCustomerSettings())
                .isHigherThan(application.getDkApprovalLevel())) {
            return createHiddenAnswer();
        }

        return mapperDelegates.get(field).apply(field, application);
    }

    public ApplicationCustomDataFieldBean getFullField(ApplicationCustomDataFieldBaseBean field) {
        String fieldIdentifier = getFieldIdentifier(field);
        if (!fieldMapping.containsKey(fieldIdentifier)) {
            throw new ApiValidationException(FIELD_NOT_FOUND_L);
        }

        return fieldMapping.get(fieldIdentifier);
    }

    protected void addField(ApplicationCustomDataFieldBean fieldBean,
            BiFunction<ApplicationCustomDataFieldBean, PropertyApplication, ApplicationCustomDataFieldAnswerBean> delegate) {
        mapperDelegates.put(fieldBean, delegate);
        fieldMapping.put(getFieldIdentifier(fieldBean), fieldBean);
    }

    protected String getFieldIdentifier(ApplicationCustomDataFieldBaseBean fieldBean) {
        return fieldBean.getDataType();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer() {
        return ApplicationCustomDataFieldAnswerBean.builder().build();
    }

    protected ApplicationCustomDataFieldAnswerBean createHiddenAnswer() {
        return ApplicationCustomDataFieldAnswerBean.builder().hidden(true).build();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer(Enum answer) {
        return ApplicationCustomDataFieldAnswerBean.builder().stringAnswer(answer != null ? PhraseAppKeyUtils.toKey(answer) : null).build();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer(Boolean answer) {
        return ApplicationCustomDataFieldAnswerBean.builder().booleanAnswer(answer).build();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer(String answer) {
        return ApplicationCustomDataFieldAnswerBean.builder().stringAnswer(answer).build();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer(Number answer) {
        return ApplicationCustomDataFieldAnswerBean.builder().numberAnswer(answer).build();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer(Date answer) {
        return ApplicationCustomDataFieldAnswerBean.builder().dateAnswer(answer).build();
    }

    protected ApplicationCustomDataFieldAnswerBean createAnswer(S3File answer) {
        return ApplicationCustomDataFieldAnswerBean.builder().fileAnswer(answer).build();
    }

    abstract List<ApplicationCustomDataFieldBaseBean> getPossibleFields(Property property);
}
