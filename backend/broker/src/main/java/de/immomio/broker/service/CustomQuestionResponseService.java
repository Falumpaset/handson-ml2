package de.immomio.broker.service;

import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionResponseRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */
@Slf4j
@Service
public class CustomQuestionResponseService {

    private final BaseCustomQuestionResponseRepository customQuestionResponseRepository;

    private final BasePriosetCustomQuestionAssociationRepository customQuestionAssociationRepository;

    @Autowired
    public CustomQuestionResponseService(
            BaseCustomQuestionResponseRepository customQuestionResponseRepository,
            BasePriosetCustomQuestionAssociationRepository customQuestionAssociationRepository
    ) {
        this.customQuestionResponseRepository = customQuestionResponseRepository;
        this.customQuestionAssociationRepository = customQuestionAssociationRepository;
    }

    public List<CustomQuestionResponse> getCustomQuestionResponses(PropertyApplication propertyApplication) {
        return getCustomQuestionResponses(propertyApplication.getProperty(), propertyApplication.getUserProfile());
    }

    public List<CustomQuestionResponse> getCustomQuestionResponses(Property property, PropertySearcherUserProfile userProfile) {
        List<PriosetCustomQuestionAssociation> associations =
                customQuestionAssociationRepository.findByPrioset(property.getPrioset());

        List<CustomQuestion> customQuestions =
                associations.stream()
                            .map(PriosetCustomQuestionAssociation::getCustomQuestion)
                            .collect(Collectors.toList());
        if (customQuestions.isEmpty()) {
            return Collections.emptyList();
        }

        return customQuestionResponseRepository.findAllByUserProfileAndCustomQuestionFetchPriosets(customQuestions, userProfile);
    }

    public CustomQuestionResponse findById(Long responseId) {
        return customQuestionResponseRepository.findById(responseId).orElse(null);
    }

}
