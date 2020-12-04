package de.immomio.model.repository.shared.propertyProposal;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;

public interface PropertyProposalRepositoryCustom {

    PropertyProposal customSave(PropertyProposal propertyApplication);

    PropertyProposal customFindOne(Long id);

    void customDeleteAllByUserProfile(PropertySearcherUserProfile userProfile);
}
