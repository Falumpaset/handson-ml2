package de.immomio.landlord.service.customquestion;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.shared.bean.common.CustomQuestionAssociationBean;
import de.immomio.model.repository.shared.customquestion.CustomQuestionRepository;
import de.immomio.model.repository.shared.customquestion.PriosetCustomQuestionAssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordCustomQuestionAssociationService {

    private static final String CUSTOM_QUESTION_NOT_FOUND = "CUSTOM_QUESTION_NOT_FOUND_L";
    public static final String CUSTOM_QUESTION_NOT_PROPERTY_L = "CUSTOM_QUESTION_NOT_PROPERTY_L";

    private final CustomQuestionRepository customQuestionRepository;

    private final PriosetCustomQuestionAssociationRepository customQuestionAssociationRepository;

    @Autowired
    public LandlordCustomQuestionAssociationService(
            CustomQuestionRepository customQuestionRepository,
            PriosetCustomQuestionAssociationRepository customQuestionAssociationRepository
    ) {
        this.customQuestionRepository = customQuestionRepository;
        this.customQuestionAssociationRepository = customQuestionAssociationRepository;
    }

    public void mergeCustomQuestionAssociations(
            Prioset prioset,
            List<CustomQuestionAssociationBean> associationBeans
    ) {
        List<PriosetCustomQuestionAssociation> associations = prioset.getCustomQuestionAssociations();

        List<PriosetCustomQuestionAssociation> associationsToSave = getAssociationsToSave(prioset, associationBeans,
                associations);

        List<PriosetCustomQuestionAssociation> associationsToDelete = getAssociationsToDelete(associationBeans,
                associations);

        customQuestionAssociationRepository.deleteAll(associationsToDelete);
        customQuestionAssociationRepository.saveAll(associationsToSave);

        List<PriosetCustomQuestionAssociation> priosetAssociations = customQuestionAssociationRepository.findByPrioset(prioset);
        prioset.setCustomQuestionAssociations(priosetAssociations);
    }

    private List<PriosetCustomQuestionAssociation> getAssociationsToDelete(
            List<CustomQuestionAssociationBean> associationBeans,
            List<PriosetCustomQuestionAssociation> associations
    ) {
        return associations.stream()
                .filter(association -> associationBeans.stream()
                        .noneMatch(associationBean -> isEqual(associationBean, association)))
                .collect(Collectors.toList());
    }

    private List<PriosetCustomQuestionAssociation> getAssociationsToSave(
            Prioset prioset,
            List<CustomQuestionAssociationBean> associationBeans,
            List<PriosetCustomQuestionAssociation> associations
    ) {
        return associationBeans.stream()
                .map(associationBean ->
                        associations.stream()
                                .filter(association -> isEqual(associationBean, association))
                                .peek(association -> setImportance(associationBean, association))
                                .findFirst()
                                .orElse(createNewAssociation(prioset, associationBean)))
                .collect(Collectors.toList());
    }

    private PriosetCustomQuestionAssociation createNewAssociation(Prioset prioset,
            CustomQuestionAssociationBean associationBean) {
        CustomQuestion customQuestion = customQuestionRepository.findById(associationBean.getCustomQuestionId())
                .orElseThrow(() -> new ApiValidationException(CUSTOM_QUESTION_NOT_FOUND));

        if (customQuestion.getType() != CustomQuestionType.PROPERTY) {
            throw new ApiValidationException(CUSTOM_QUESTION_NOT_PROPERTY_L);
        }

        PriosetCustomQuestionAssociation association = new PriosetCustomQuestionAssociation();
        association.setPrioset(prioset);
        association.setCustomQuestion(customQuestion);
        setImportance(associationBean, association);
        return association;
    }

    private void setImportance(
            CustomQuestionAssociationBean associationBean,
            PriosetCustomQuestionAssociation association
    ) {
        if (associationBean.getImportance() < 0 || associationBean.getImportance() > 10){
            throw new ApiValidationException("CUSTOM_QUESTION_ASSOCIATION_NOT_IN_RANGE_L");
        }

        association.setImportance(associationBean.getImportance());
    }

    private boolean isEqual(
            CustomQuestionAssociationBean associationBean,
            PriosetCustomQuestionAssociation association
    ) {
        return Objects.equals(association.getCustomQuestion().getId(),
                associationBean.getCustomQuestionId());
    }
}
