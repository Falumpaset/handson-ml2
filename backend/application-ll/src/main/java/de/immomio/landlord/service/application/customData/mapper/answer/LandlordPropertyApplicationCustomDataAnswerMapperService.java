package de.immomio.landlord.service.application.customData.mapper.answer;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBundle;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldAnswerBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LandlordPropertyApplicationCustomDataAnswerMapperService {

    private static final String FIELD_TYPE_NOT_FOUND_L = "FIELD_TYPE_NOT_FOUND_L";

    private final Map<ApplicationCustomDataFieldType, ApplicationCustomDataMapperDelegate> mappers;

    @Autowired
    public LandlordPropertyApplicationCustomDataAnswerMapperService(List<ApplicationCustomDataMapperDelegate> customDataMapperDelegates) {
        mappers = customDataMapperDelegates.stream().collect(Collectors.toMap(ApplicationCustomDataMapperDelegate::getType, Function.identity()));
    }

    public List<ApplicationCustomDataFieldBaseBean> getPossibleFields(Property property) {
        return Arrays.stream(ApplicationCustomDataFieldType.values())
                .map(mappers::get)
                .map(applicationCustomDataMapperDelegate -> applicationCustomDataMapperDelegate.getPossibleFields(property))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public ApplicationCustomDataBundle mapApplicationsToFields(List<ApplicationCustomDataFieldBaseBean> fields,
            List<PropertyApplication> propertyApplications,
            boolean anonymised) {
        List<ApplicationCustomDataFieldBean> fieldsToUse = fields.stream()
                .map(this::getFullField)
                .filter(field -> !(field.isAnonymisable() && anonymised))
                .collect(Collectors.toList());

        return ApplicationCustomDataBundle.builder()
                .fields(fieldsToUse)
                .data(propertyApplications.stream().map(application -> mapApplicationToFields(fieldsToUse, application)).collect(Collectors.toList()))
                .build();
    }

    private ApplicationCustomDataBean mapApplicationToFields(List<ApplicationCustomDataFieldBean> fields, PropertyApplication propertyApplication) {
        return ApplicationCustomDataBean.builder()
                .applicationId(propertyApplication.getId())
                .fieldData(fields.stream().map(field -> apply(field, propertyApplication)).collect(Collectors.toList()))
                .build();
    }

    private ApplicationCustomDataFieldAnswerBean apply(ApplicationCustomDataFieldBean field, PropertyApplication propertyApplication) {
        if (!mappers.containsKey(field.getFieldType())) {
            throw new ApiValidationException(FIELD_TYPE_NOT_FOUND_L);
        }

        return mappers.get(field.getFieldType()).apply(field, propertyApplication);
    }

    private ApplicationCustomDataFieldBean getFullField(ApplicationCustomDataFieldBaseBean field) {
        if (!mappers.containsKey(field.getFieldType())) {
            throw new ApiValidationException(FIELD_TYPE_NOT_FOUND_L);
        }

        return mappers.get(field.getFieldType()).getFullField(field);
    }
}
