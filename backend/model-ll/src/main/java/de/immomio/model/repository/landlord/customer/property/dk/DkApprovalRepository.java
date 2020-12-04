package de.immomio.model.repository.landlord.customer.property.dk;

import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.property.dk.DkApproval;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.core.landlord.customer.property.dk.BaseDkApprovalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "dkApprovals")
public interface DkApprovalRepository extends BaseDkApprovalRepository {

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.application a WHERE o.id = :id AND a.property.customer.id = ?#{principal.customer.id}")
    Optional<DkApproval> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o INNER JOIN o.application a WHERE a.property.customer.id = ?#{principal.customer.id}")
    Page<DkApproval> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#dkApproval.application.property.customer == principal?.customer")
    DkApproval save(@Param("dkApproval") DkApproval dkApproval);

    @RestResource(exported = false)
    List<DkApproval> findByApplication(PropertyApplication application);

    @RestResource(exported = false)
    @Query("SELECT a.level from DkApproval a where a.application = :application")
    List<DkApprovalLevel> findLevelByApplication(PropertyApplication application, Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT COALESCE ((select count(*) from " +
                        "(select distinct on (" +
                            "da.application_id) da.application_id, da.id, da.level, ap.property_id " +
                            "from landlord.dk_approval da " +
                            "inner join shared.application ap on ap.id = da.application_id " +
                            "order by da.application_id, id desc" +
                        ") as dk_app " +
                    "group by dk_app.property_id, dk_app.level " +
                    "having dk_app.property_id = :propertyId and CAST(dk_app.level AS TEXT)= :dkLevel), 0)"
    )
    Long countApprovedByPropertyAndLevel(@Param("propertyId") Long propertyId, @Param("dkLevel") String dkLevel);

}
