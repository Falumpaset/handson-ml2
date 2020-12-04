package de.immomio.model.repository.shared.selfdisclosure;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.model.repository.core.shared.selfdisclosure.BaseSelfDisclosureResponseRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RestResource(exported = false)
public interface SelfDisclosureResponseRepository extends BaseSelfDisclosureResponseRepository {

    Optional<SelfDisclosureResponse> findFirstByPropertyAndUserProfile(Property property, PropertySearcherUserProfile userProfile);
    List<SelfDisclosureResponse> findAllByUserProfile(PropertySearcherUserProfile userProfile);
}
