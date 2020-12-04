package de.immomio.service.calculator;

import de.immomio.data.base.bean.score.ScoreBean;
import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.common.customQuestion.CustomQuestionResponse;
import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionRepository;
import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionResponseRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
public abstract class AbstractCalculatorDelegate <
        QRR extends BaseCustomQuestionResponseRepository,
        QAR extends BasePriosetCustomQuestionAssociationRepository,
        CQR extends BaseCustomQuestionRepository> {

    private QRR responseRepository;
    private QAR associationRepository;
    private CQR customQuestionRepository;
    private CalculationExecutor calculationExecutor;

    public AbstractCalculatorDelegate(QRR responseRepository, QAR associationRepository, CQR customQuestionRepository, CalculationExecutor calculationExecutor) {
        this.responseRepository = responseRepository;
        this.associationRepository = associationRepository;
        this.customQuestionRepository = customQuestionRepository;
        this.calculationExecutor = calculationExecutor;
    }

    public ScoreBean calculateScore(Property property, PropertySearcherUserProfile userProfile) {
        List<PriosetCustomQuestionAssociation> associations = associationRepository.findByPrioset(property.getPrioset());

        List<CustomQuestion> globalQuestions = customQuestionRepository.findGlobalForCustomer(property.getCustomer());
        List<CustomQuestionResponse> globalResponses = !globalQuestions.isEmpty() ? responseRepository.findAllByUserProfileAndCustomQuestion(userProfile, globalQuestions) : new ArrayList<>();

        List<CustomQuestion> customQuestions = associations.stream().map(PriosetCustomQuestionAssociation::getCustomQuestion).collect(Collectors.toList());
        List<CustomQuestionResponse> customQuestionResponses = !customQuestions.isEmpty() ? responseRepository.findAllByUserProfileAndCustomQuestionFetchPriosets(customQuestions, userProfile) : new ArrayList<>();

        customQuestionResponses.addAll(globalResponses);

        return calculationExecutor.calculateScore(property, userProfile, customQuestionResponses, associations, globalQuestions);
    }


}
