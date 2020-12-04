package de.immomio.model.repository.landlord.schufa;

import de.immomio.data.landlord.entity.schufa.LandlordSchufaAccount;
import de.immomio.model.repository.core.landlord.schufa.BaseLandlordSchufaAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "schufaAccounts")
public interface LandlordSchufaAccountRepository extends BaseLandlordSchufaAccountRepository {

    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    LandlordSchufaAccount findOne();

    @Override
    @RestResource(exported = false)
    Page<LandlordSchufaAccount> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends LandlordSchufaAccount> S save(S entity);

    @Override
    @PreAuthorize("#schufaAccount.customer.id == principal?.customer.id")
    void delete(LandlordSchufaAccount schufaAccount);
}
