package de.immomio.model.repository.shared.searchprofile;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherSearchProfile;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.NonNullApi;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "searchProfiles")
public interface PropertySearcherSearchProfileRepository extends BasePropertySearcherSearchProfileRepository,
        PropertySearcherSearchProfileRepositoryCustom {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.userProfile.user.id = ?#{principal.id}")
    Optional<PropertySearcherSearchProfile> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.userProfile.user.id = ?#{principal.id} and o.deleted = false")
    Page<PropertySearcherSearchProfile> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o  WHERE o.userProfile.user.id = ?#{principal.id} and o.deleted = false")
    List<PropertySearcherSearchProfile> findAll();

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#searchProfile?.userProfile?.user.id == principal?.id")
    void delete(@Param("searchProfile") PropertySearcherSearchProfile searchProfile);

    @Override
    @RestResource(exported = false)
    PropertySearcherSearchProfile save(@Param("searchProfile") PropertySearcherSearchProfile searchProfile);

    @Modifying
    @Transactional
    @Query(value = "UPDATE propertysearcher.searchprofile set deleted = true where user_profile_id = :userProfileId ", nativeQuery = true)
    void customDeleteByUserProfileId(@Param("userProfileId") Long userProfileId);

}
