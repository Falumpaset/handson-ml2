package de.immomio.model.repository.service.landlord.customer.credential;

import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.core.landlord.customer.credential.BaseCredentialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ll-credentials")
public interface CredentialRepository extends BaseCredentialRepository {

    Page<Credential> findByCustomer(@Param("customer") LandlordCustomer customer, Pageable pageable);

}
