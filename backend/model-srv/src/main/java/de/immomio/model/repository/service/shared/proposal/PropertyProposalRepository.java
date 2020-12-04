package de.immomio.model.repository.service.shared.proposal;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Maik Kingma
 */

@RepositoryRestResource(path = "proposals")
public interface PropertyProposalRepository extends JpaRepository<PropertyProposal, Long> {

    List<PropertyProposal> findByProperty(Property property);
}
