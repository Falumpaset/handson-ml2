package de.immomio.service.customQuestion;

import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseQuestionBean;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.model.repository.shared.customquestion.CustomQuestionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Service
public class CustomQuestionResponseService {

    private final CustomQuestionResponseRepository customQuestionResponseRepository;

    private final CustomQuestionConverter customQuestionConverter;

    @Autowired
    public CustomQuestionResponseService(CustomQuestionResponseRepository customQuestionResponseRepository, CustomQuestionConverter customQuestionConverter) {
        this.customQuestionResponseRepository = customQuestionResponseRepository;
        this.customQuestionConverter = customQuestionConverter;
    }

    public List<CustomQuestionResponseQuestionBean> getCustomQuestionResponses(Property property, PropertySearcherUserProfile userProfile) {
        List<CustomQuestionResponseQuestionBean> customQuestionResponseBeans = getGlobalQuestionResponses(property, userProfile);
        List<CustomQuestion> customQuestions =
                property.getPrioset().getCustomQuestionAssociations()
                        .stream()
                        .map(PriosetCustomQuestionAssociation::getCustomQuestion)
                        .collect(Collectors.toList());
        if (!customQuestions.isEmpty()) {
            List<CustomQuestionResponse> customQuestionResponses = customQuestionResponseRepository.findAllByUserProfileAndCustomQuestion(userProfile, customQuestions);
            customQuestionResponseBeans.addAll(customQuestionResponses.stream().map(customQuestionConverter::getCustomQuestionResponseQuestionBean).collect(Collectors.toList()));
        }

        return customQuestionResponseBeans;
    }

    public List<CustomQuestionResponseQuestionBean> getCustomQuestionResponses(PropertySearcherUserProfile userProfile) {
        List<CustomQuestionResponse> responses = customQuestionResponseRepository.findAllByUserProfile(userProfile);

        return responses.stream().map(customQuestionConverter::getCustomQuestionResponseQuestionBean).collect(Collectors.toList());
    }

    public List<CustomQuestionResponseQuestionBean> getGlobalQuestionResponses(Property property, PropertySearcherUserProfile userProfile) {
        List<CustomQuestionResponse> globalResponses = customQuestionResponseRepository.findGlobalByUserProfileAndCustomer(userProfile, property.getCustomer());
        return globalResponses.stream().map(customQuestionConverter::getCustomQuestionResponseQuestionBean).collect(Collectors.toList());
    }

}
