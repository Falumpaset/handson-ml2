package de.immomio.model.repository.service.shared.selfdisclosure;

import de.immomio.model.repository.core.shared.selfdisclosure.BaseSelfDisclosureRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface SelfDisclosureRepository extends BaseSelfDisclosureRepository {

}
