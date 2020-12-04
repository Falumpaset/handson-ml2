package de.immomio.model.repository.core.shared.priosetCustomQuestion;

import de.immomio.data.landlord.entity.customquestion.CustomQuestion;
import de.immomio.data.landlord.entity.customquestion.PriosetCustomQuestionAssociation;
import de.immomio.data.landlord.entity.prioset.Prioset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Fabian Beck
 */

@Repository
public interface BasePriosetCustomQuestionAssociationRepository
        extends JpaRepository<PriosetCustomQuestionAssociation, Long> {

    List<PriosetCustomQuestionAssociation> findByPrioset(Prioset prioset);

    List<PriosetCustomQuestionAssociation> findByCustomQuestion(CustomQuestion customQuestion);

}
