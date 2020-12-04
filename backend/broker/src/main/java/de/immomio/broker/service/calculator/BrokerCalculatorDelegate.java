package de.immomio.broker.service.calculator;

import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionRepository;
import de.immomio.model.repository.core.shared.customquestion.BaseCustomQuestionResponseRepository;
import de.immomio.model.repository.core.shared.priosetCustomQuestion.BasePriosetCustomQuestionAssociationRepository;
import de.immomio.service.calculator.AbstractCalculatorDelegate;
import de.immomio.service.calculator.CalculationExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class BrokerCalculatorDelegate extends AbstractCalculatorDelegate<BaseCustomQuestionResponseRepository, BasePriosetCustomQuestionAssociationRepository, BaseCustomQuestionRepository> {

    @Autowired
    public BrokerCalculatorDelegate(BaseCustomQuestionResponseRepository responseRepository, BasePriosetCustomQuestionAssociationRepository associationRepository, CalculationExecutor calculationExecutor, BaseCustomQuestionRepository customQuestionRepository) {
        super(responseRepository, associationRepository, customQuestionRepository, calculationExecutor);
    }
}
