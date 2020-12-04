package de.immomio.model.repository.core.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface BaseSelfDisclosureQuestionConfigurationRepository extends JpaRepository<SelfDisclosureQuestionConfiguration, Long> {
}
