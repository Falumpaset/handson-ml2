package de.immomio.service.landlord.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureQuestionBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureResponseBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureSubQuestionBean;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestion;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionConfiguration;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestion;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionConfiguration;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.service.landlord.property.PropertyConverter;
import de.immomio.service.propertysearcher.userprofile.PropertySearcherUserProfileConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class SelfDisclosureDataConverter {

    private PropertyConverter propertyConverter;
    private PropertySearcherUserProfileConverter userProfileConverter;

    @Autowired
    public SelfDisclosureDataConverter(PropertyConverter propertyConverter,
            PropertySearcherUserProfileConverter userProfileConverter) {
        this.propertyConverter = propertyConverter;
        this.userProfileConverter = userProfileConverter;
    }

    public SelfDisclosureBean selfDisclosureToBean(SelfDisclosure selfDisclosure) {
        return SelfDisclosureBean.builder()
                .id(selfDisclosure.getId())
                .feedbackEmail(selfDisclosure.getFeedbackEmail())
                .description(selfDisclosure.getDescription())
                .documents(selfDisclosure.getDocuments())
                .confirmations(selfDisclosure.getConfirmations())
                .questions(selfDisclosure.getQuestionsConfigs().stream()
                        .map(this::selfDisclosureQuestionMappingToBean)
                        .collect(Collectors.toList())).build();
    }

    public SelfDisclosureQuestionBean selfDisclosureQuestionToBean(SelfDisclosureQuestion question) {
        return SelfDisclosureQuestionBean.builder()
                .id(question.getId())
                .title(question.getTitle())
                .type(question.getType())
                .subQuestions(question.getSubQuestions().stream().map(this::selfDisclosureSubQuestionToBean).collect(Collectors.toList()))
                .commentAllowed(question.getCommentAllowed())
                .commentHint(question.getCommentHint())
                .orderNumber(question.getOrderNumber())
                .uploadAllowed(question.getUploadAllowed())
                .uploadHint(question.getUploadHint())
                .hidden(true)
                .mandatory(false)
                .build();
    }

    public SelfDisclosureQuestionBean selfDisclosureQuestionMappingToBean(SelfDisclosureQuestionConfiguration mapping) {
        SelfDisclosureQuestionBean selfDisclosureQuestionBean = selfDisclosureQuestionToBean(mapping.getQuestion());
        selfDisclosureQuestionBean.setHidden(mapping.isHidden());
        selfDisclosureQuestionBean.setMandatory(mapping.isMandatory());
        selfDisclosureQuestionBean.setSubQuestions(
                mapping.getSubQuestions().stream().map(this::selfDisclosureSubQuestionMappingToBean).collect(Collectors.toList()));
        return selfDisclosureQuestionBean;
    }

    public SelfDisclosureSubQuestionBean selfDisclosureSubQuestionToBean(SelfDisclosureSubQuestion subQuestion) {
        return SelfDisclosureSubQuestionBean.builder()
                .id(subQuestion.getId())
                .title(subQuestion.getTitle())
                .type(subQuestion.getType())
                .commentAllowed(subQuestion.getCommentAllowed())
                .commentHint(subQuestion.getCommentHint())
                .orderNumber(subQuestion.getOrderNumber())
                .constantName(subQuestion.getConstantName())
                .hidden(true)
                .mandatory(false)
                .build();
    }

    public SelfDisclosureSubQuestionBean selfDisclosureSubQuestionMappingToBean(SelfDisclosureSubQuestionConfiguration subQuestionMapping) {
        SelfDisclosureSubQuestionBean subQuestionBean = selfDisclosureSubQuestionToBean(subQuestionMapping.getSubQuestion());
        subQuestionBean.setHidden(subQuestionMapping.isHidden());
        subQuestionBean.setMandatory(subQuestionMapping.isMandatory());
        return subQuestionBean;
    }

    public SelfDisclosureResponseBean selfDisclosureResponseToBean(SelfDisclosureResponse response) {
        return SelfDisclosureResponseBean.builder()
                .id(response.getId())
                .data(response.getData())
                .selfDisclosure(selfDisclosureToBean(response.getSelfDisclosure()))
                .property(propertyConverter.convertToPropertyBean(response.getProperty()))
                .user(userProfileConverter.convertUserProfile(response.getUserProfile()))
                .build();
    }

}
