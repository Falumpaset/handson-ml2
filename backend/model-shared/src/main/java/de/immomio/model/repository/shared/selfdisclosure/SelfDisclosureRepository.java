package de.immomio.model.repository.shared.selfdisclosure;

import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.model.repository.core.shared.selfdisclosure.BaseSelfDisclosureRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RestResource(exported = false)
public interface SelfDisclosureRepository extends BaseSelfDisclosureRepository {

    @Override
    @PostAuthorize("returnObject?.get()?.customer.id == principal?.customer.id")
    Optional<SelfDisclosure> findById(Long id);

    @Override
    @PreAuthorize("#selfDisclosure.customer.id == principal?.customer?.id")
    <T extends SelfDisclosure> T save(@Param("selfDisclosure") T selfDisclosure);

}
