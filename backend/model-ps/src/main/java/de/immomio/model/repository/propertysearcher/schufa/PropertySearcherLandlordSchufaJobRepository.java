package de.immomio.model.repository.propertysearcher.schufa;

import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.core.landlord.schufa.BaseLandlordSchufaJobRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author Niklas Lindemann
 */
@RestResource(exported = false)
@RepositoryRestResource(path = "schufaJobs")
public interface PropertySearcherLandlordSchufaJobRepository extends BaseLandlordSchufaJobRepository {

    @RestResource(exported = false)
    List<LandlordSchufaJob> findByUserProfileIn(List<PropertySearcherUserProfile> userProfiles);

}
