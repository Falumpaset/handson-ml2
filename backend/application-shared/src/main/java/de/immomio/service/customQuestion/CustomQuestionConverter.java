package de.immomio.service.customQuestion;

import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseBean;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionResponseQuestionBean;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class CustomQuestionConverter {

    public CustomQuestionBean getCustomQuestionBean(CustomQuestion customQuestion) {
        return CustomQuestionBean.builder()
                .commentHint(customQuestion.getCommentHint())
                .desiredResponses(customQuestion.getDesiredResponses())
                .form(customQuestion.getForm())
                .global(customQuestion.getType() == CustomQuestionType.GLOBAL)
                .id(customQuestion.getId())
                .importance(customQuestion.getImportance())
                .schema(customQuestion.getSchema())
                .commentAllowed(customQuestion.getCommentAllowed())
                .scoring(customQuestion.getScoring())
                .build();
    }

    public CustomQuestionResponseBean getCustomQuestionResponseBean(CustomQuestionResponse customQuestionResponse) {
        return CustomQuestionResponseBean.builder()
                .comment(customQuestionResponse.getComment())
                .responses(customQuestionResponse.getData())
                .selectedRange(customQuestionResponse.getSelectedRange()).build();
    }

    public CustomQuestionResponseQuestionBean getCustomQuestionResponseQuestionBean(CustomQuestionResponse customQuestionResponse) {
        CustomQuestionResponseQuestionBean responseQuestionBean = new CustomQuestionResponseQuestionBean();
        responseQuestionBean.setCustomQuestion(getCustomQuestionBean(customQuestionResponse.getCustomQuestion()));
        responseQuestionBean.setComment(customQuestionResponse.getComment());
        responseQuestionBean.setResponses(customQuestionResponse.getData());
        responseQuestionBean.setSelectedRange(customQuestionResponse.getSelectedRange());
        return responseQuestionBean;
    }
}
