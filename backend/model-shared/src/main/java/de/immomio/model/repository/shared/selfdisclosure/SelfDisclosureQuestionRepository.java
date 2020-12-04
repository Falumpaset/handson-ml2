package de.immomio.model.repository.shared.selfdisclosure;

import de.immomio.model.repository.core.shared.selfdisclosure.BaseSelfDisclosureQuestionRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface SelfDisclosureQuestionRepository extends BaseSelfDisclosureQuestionRepository {
}
