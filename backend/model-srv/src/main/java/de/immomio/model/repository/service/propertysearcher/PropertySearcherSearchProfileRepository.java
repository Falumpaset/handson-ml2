package de.immomio.model.repository.service.propertysearcher;

import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "ps-searchProfiles")
public interface PropertySearcherSearchProfileRepository extends BasePropertySearcherSearchProfileRepository {

}
