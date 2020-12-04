package de.immomio.service.calculator;

import de.immomio.model.repository.shared.customquestion.CustomQuestionRepository;
import de.immomio.model.repository.shared.customquestion.CustomQuestionResponseRepository;
import de.immomio.model.repository.shared.customquestion.PriosetCustomQuestionAssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */
@Service
public class PropertySearcherCalculatorDelegate extends AbstractCalculatorDelegate<CustomQuestionResponseRepository, PriosetCustomQuestionAssociationRepository, CustomQuestionRepository> {

    @Autowired
    public PropertySearcherCalculatorDelegate(CustomQuestionResponseRepository responseRepository, PriosetCustomQuestionAssociationRepository associationRepository, CalculationExecutor calculationExecutor, CustomQuestionRepository customQuestionRepository) {
        super(responseRepository, associationRepository, customQuestionRepository, calculationExecutor);
    }
}
