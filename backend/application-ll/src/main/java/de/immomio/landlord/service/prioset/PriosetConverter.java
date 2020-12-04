package de.immomio.landlord.service.prioset;

import de.immomio.data.landlord.bean.prioset.PriosetBean;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriosetConverter {

    public PriosetBean priosetToBean(Prioset prioset) {
        return PriosetBean.builder()
                .id(prioset.getId())
                .name(prioset.getName())
                .description(prioset.getDescription())
                .template(prioset.getTemplate())
                .data(prioset.getData())
                .customQuestions(getCustomQuestionBeans(prioset))
                .build();
    }

    private List<CustomQuestionBean> getCustomQuestionBeans(Prioset prioset) {
        return prioset.getCustomQuestionAssociations()
                .stream()
                .map(this::customQuestionAssociationToBean)
                .collect(Collectors.toList());
    }

    private CustomQuestionBean customQuestionAssociationToBean(PriosetCustomQuestionAssociation association) {
        CustomQuestion customQuestion = association.getCustomQuestion();
        return CustomQuestionBean.builder()
                .id(customQuestion.getId())
                .schema(customQuestion.getSchema())
                .form(customQuestion.getForm())
                .desiredResponses(customQuestion.getDesiredResponses())
                .commentHint(customQuestion.getCommentHint())
                .global(false)
                .importance(association.getImportance())
                .scoring(association.getCustomQuestion().getScoring())
                .build();
    }
}
