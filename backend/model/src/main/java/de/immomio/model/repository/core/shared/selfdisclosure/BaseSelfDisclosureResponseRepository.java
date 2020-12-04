package de.immomio.model.repository.core.shared.selfdisclosure;

import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface BaseSelfDisclosureResponseRepository extends JpaRepository<SelfDisclosureResponse, Long> {

    @Query(value = "select *" +
                   " from shared.self_disclosure_response sdr " +
                   " inner join shared.self_disclosure sd on sd.id = sdr.self_disclosure_id " +
                   " inner join landlord.property p on p.id = sdr.property_id " +
                   " and sdr.user_profile_id = :userProfileId and p.id = :propertyId ",
           nativeQuery = true)
    Optional<SelfDisclosureResponse> findByPropertyIdAndUserProfileId(
            @Param("propertyId") Long propertyId,
            @Param("userProfileId") Long userProfileId
    );

}
