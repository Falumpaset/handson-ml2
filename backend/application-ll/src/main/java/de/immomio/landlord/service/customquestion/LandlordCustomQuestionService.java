package de.immomio.landlord.service.customquestion;

import de.immomio.beans.landlord.CustomQuestionCreateBean;
import de.immomio.constants.exceptions.CustomQuestionValidationException;
import de.immomio.data.base.json.JsonPayload;
import de.immomio.data.base.type.customQuestion.CustomQuestionType;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.shared.bean.customQuestion.CustomQuestionBean;
import de.immomio.landlord.service.sender.LandlordPropertyScoreRefreshSender;
import de.immomio.model.repository.shared.customquestion.CustomQuestionRepository;
import de.immomio.service.customQuestion.CustomQuestionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maik Kingma
 */

@Service
public class LandlordCustomQuestionService {

    public static final String DESIRED_RESPONSE_KEY_DOES_NOT_MATCH_QUESTION_KEY =
            "DESIRED_RESPONSE_KEY_DOES_NOT_MATCH_QUESTION_KEY_L";
    public static final String MORE_THAN_ONE_QUESTION_IN_SCHEMA = "MORE_THAN_ONE_QUESTION_IN_SCHEMA_L";
    public static final String MORE_THAN_ONE_RESPONSE_KEY = "MORE_THAN_ONE_RESPONSE_KEY_L";
    public static final String RESPONSE_NOT_CONTAINED_IN_GIVEN_RESPONSE_SELECTION =
            "RESPONSE_NOT_CONTAINED_IN_GIVEN_RESPONSE_SELECTION_L";
    public static final String SCORING_TURNED_ON_BUT_DESIRED_ANSWERS_UNDEFINED =
            "SCORING_TURNED_ON_BUT_DESIRED_ANSWERS_UNDEFINED_L";
    public static final String SCHEMA_OR_FORM_NOT_PROVIDED = "SCHEMA_OR_FORM_NOT_PROVIDED_L";

    private final CustomQuestionRepository customQuestionRepository;

    private final LandlordCustomQuestionValidator validator;

    private final LandlordPropertyScoreRefreshSender propertyScoreRefreshSender;

    private final CustomQuestionConverter customQuestionConverter;

    @Autowired
    public LandlordCustomQuestionService(
            CustomQuestionRepository customQuestionRepository,
            LandlordCustomQuestionValidator validator,
            LandlordPropertyScoreRefreshSender propertyScoreRefreshSender,
            CustomQuestionConverter customQuestionConverter) {
        this.customQuestionRepository = customQuestionRepository;
        this.validator = validator;
        this.propertyScoreRefreshSender = propertyScoreRefreshSender;
        this.customQuestionConverter = customQuestionConverter;
    }

    public CustomQuestion createCustomQuestion(CustomQuestionCreateBean questionBean, LandlordCustomer customer)
            throws CustomQuestionValidationException {
        JsonPayload jsonPayload = questionBean.getJsonPayload();
        if (jsonPayload.getSchema() == null || jsonPayload.getForm() == null) {
            throw new CustomQuestionValidationException(SCHEMA_OR_FORM_NOT_PROVIDED);
        }
        validator.validateScoringModel(questionBean);
        validator.validateGlobalQuestion(questionBean);
        validator.validateQuestionSchema(jsonPayload.getSchema());
        validator.validateQuestionResponseModel(jsonPayload.getSchema(), questionBean.getDesiredResponses());

        CustomQuestion newQuestion = new CustomQuestion();
        newQuestion.setCustomer(customer);
        boolean scoring = questionBean.isScoring();
        newQuestion.setScoring(scoring);
        newQuestion.setSchema(jsonPayload.getSchema());
        newQuestion.setForm(jsonPayload.getForm());
        newQuestion.setDesiredResponses(questionBean.getDesiredResponses());
        newQuestion.setCommentAllowed(questionBean.isCommentAllowed());
        newQuestion.setCommentHint(questionBean.getCommentHint());
        newQuestion.setType(questionBean.isGlobal() ? CustomQuestionType.GLOBAL : CustomQuestionType.PROPERTY);
        newQuestion.setImportance(questionBean.getImportance());

        newQuestion = save(newQuestion);

        if (newQuestion.getType() == CustomQuestionType.GLOBAL) {
            propertyScoreRefreshSender.sendUpdateMessage(customer);
        }

        return newQuestion;
    }

    public CustomQuestion save(CustomQuestion question) {
        return customQuestionRepository.save(question);
    }

    public void delete(CustomQuestion customQuestion) {
        if (customQuestion.getType() == CustomQuestionType.GLOBAL) {
            deleteGlobalCustomQuestion(customQuestion);
        } else {
            deleteCustomQuestion(customQuestion);
        }
    }

    private void deleteGlobalCustomQuestion(CustomQuestion customQuestion) {
        LandlordCustomer customer = customQuestion.getCustomer();

        customQuestionRepository.delete(customQuestion);

        propertyScoreRefreshSender.sendUpdateMessage(customer);
    }

    private void deleteCustomQuestion(CustomQuestion customQuestion) {
        List<Prioset> priosets = customQuestion.getPriosets().stream().map(PriosetCustomQuestionAssociation::getPrioset).collect(Collectors.toList());

        customQuestionRepository.delete(customQuestion);

        propertyScoreRefreshSender.sendUpdateMessageForPriosets(priosets);
    }

    public Page<CustomQuestionBean> findAll(LandlordCustomer customer, Boolean global, Pageable pageable) {
        Page<CustomQuestion> customQuestions = customQuestionRepository.findByCustomerAndType(customer, global ? CustomQuestionType.GLOBAL : CustomQuestionType.PROPERTY, pageable);

        PageImpl<CustomQuestionBean> customQuestionBeans = new PageImpl<>(customQuestions.getContent().stream().map(customQuestionConverter::getCustomQuestionBean).collect(Collectors.toList()), pageable, customQuestions.getTotalElements());
        return customQuestionBeans;
    }
}
