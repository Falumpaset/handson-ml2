package de.immomio.model.repository.shared.tenant;

import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.model.repository.core.shared.tenant.BasePropertyTenantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "propertyTenants")
public interface PropertyTenantRepository extends BasePropertyTenantRepository {

    String PROPERTY = "property";
    String USER = "user";
    String CONTRACT_START = "contractStart";

    @Override
    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.property.customer.id = ?#{principal.customer.id} " +
            "OR o.userProfile.user.id = ?#{principal.id}")
    Page<PropertyTenant> findAll(Pageable pageable);

    @Override
    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.id = :id " +
            "AND o.property.customer.id = ?#{principal.customer.id} OR o.userProfile.user.id = ?#{principal.id}")
    Optional<PropertyTenant> findById(Long id);

    @PreAuthorize("#property.customer == principal?.customer")
    Page<PropertyTenant> findByProperty(
            @Param(PROPERTY) Property property,
            Pageable pageable
    );

    @PreAuthorize("#userProfile.user.id == principal?.id")
    Page<PropertyTenant> findByUserProfile(
            @Param("userProfile") PropertySearcherUserProfile userProfile,
            Pageable pageable
    );

    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.property.customer.id = ?#{principal.customer.id} " +
            "AND o.contractStart <= :" + CONTRACT_START)
    Page<PropertyTenant> findAllByContractStartBefore(
            @DateTimeFormat(pattern = "dd-MM-yyyy") @Param(value = CONTRACT_START) Date start,
            Pageable pageable
    );

    @Query("SELECT o FROM #{#entityName} o " +
            "WHERE o.property.customer.id = ?#{principal.customer.id} " +
            "AND o.contractStart >= :" + CONTRACT_START)
    Page<PropertyTenant> findAllByContractStartAfter(
            @DateTimeFormat(pattern = "dd-MM-yyyy") @Param(value = CONTRACT_START) Date start,
            Pageable pageable
    );

    @RestResource(exported = false)
    @Query("SELECT pt from PropertyTenant pt where pt.property.customer = ?#{principal.customer} and pt.created > :date and pt.userProfile is not null")
    List<PropertyTenant> findAllCreatedForExternalApi(@Param("date") Date date);

    @Override
    @RestResource(exported = false)
    void delete(PropertyTenant entity);

    @RestResource(exported = false)
    List<PropertyTenant> findAllByUserProfileIn(List<PropertySearcherUserProfile> profiles);
}
