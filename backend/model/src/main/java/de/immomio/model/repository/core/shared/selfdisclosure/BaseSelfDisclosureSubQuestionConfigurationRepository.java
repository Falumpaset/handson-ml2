package de.immomio.model.repository.core.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionConfiguration;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestion;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseSelfDisclosureSubQuestionConfigurationRepository extends JpaRepository<SelfDisclosureSubQuestionConfiguration, Long> {
    Optional<SelfDisclosureSubQuestionConfiguration> findByQuestionConfigurationAndSubQuestion(SelfDisclosureQuestionConfiguration questionConfiguration, SelfDisclosureSubQuestion subQuestion);
}
