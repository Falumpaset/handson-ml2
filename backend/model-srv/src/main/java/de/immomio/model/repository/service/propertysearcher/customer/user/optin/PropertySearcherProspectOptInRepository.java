package de.immomio.model.repository.service.propertysearcher.customer.user.optin;

import de.immomio.model.repository.core.propertysearcher.user.optin.BasePropertySearcherProspectOptInRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "prospectOptIns")
public interface PropertySearcherProspectOptInRepository extends BasePropertySearcherProspectOptInRepository {

}
