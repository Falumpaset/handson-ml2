package de.immomio.model.repository.core.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseSelfDisclosureSubQuestionRepository extends JpaRepository<SelfDisclosureSubQuestion, Long> {
}
