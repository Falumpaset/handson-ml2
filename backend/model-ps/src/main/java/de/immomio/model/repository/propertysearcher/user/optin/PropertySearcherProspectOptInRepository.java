package de.immomio.model.repository.propertysearcher.user.optin;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherProspectOptIn;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.repository.core.propertysearcher.user.optin.BasePropertySearcherProspectOptInRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "prospectOptIns")
public interface PropertySearcherProspectOptInRepository extends BasePropertySearcherProspectOptInRepository,
        PropertySearcherProspectOptInRepositoryCustom {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.user.id = ?#{principal.id}")
    Optional<PropertySearcherProspectOptIn> findById(@Param("id") Long id);

    @Override
    @PreAuthorize("#user.id == principal?.id")
    PropertySearcherProspectOptIn findByUser(@Param("user") PropertySearcherUser user);

    @Override
    @PreAuthorize("#prospectOptIn?.user?.id == principal?.id")
    PropertySearcherProspectOptIn save(@Param("prospectOptIn") PropertySearcherProspectOptIn prospectOptIn);

    @Override
    @RestResource(exported = false)
    Page<PropertySearcherProspectOptIn> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    List<PropertySearcherProspectOptIn> findAll();

    @Override
    @RestResource(exported = false)
    void delete(PropertySearcherProspectOptIn entity);
}
