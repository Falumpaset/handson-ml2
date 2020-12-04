package de.immomio.model.repository.landlord.propertysearcher.searchprofile;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */
@Repository
public interface LandlordPropertySearcherUserSearchProfileRepository extends BasePropertySearcherSearchProfileRepository {

    @RestResource(exported = false)
    @Query("SELECT min(created) from PropertySearcherSearchProfile where deleted = false and userProfile = :profile")
    Date getFirstCreatedDate(@Param("profile") PropertySearcherUserProfile userProfile);
}
