package de.immomio.model.repository.core.landlord.schufa;

import de.immomio.data.landlord.entity.schufa.LandlordSchufaAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "schufaAccounts", exported = false)
public interface BaseLandlordSchufaAccountRepository extends JpaRepository<LandlordSchufaAccount, Long> {
}
