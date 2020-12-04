package de.immomio.model.repository.landlord.schufa;

import de.immomio.data.landlord.entity.schufa.LandlordSchufaJob;
import de.immomio.model.repository.core.landlord.schufa.BaseLandlordSchufaJobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Niklas Lindemann
 */
@RestResource(exported = false)
@RepositoryRestResource(path = "schufaJobs")
public interface LandlordSchufaJobRepository extends BaseLandlordSchufaJobRepository {

    @Override
    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id} ORDER BY ?#{#pageable}")
    Page<LandlordSchufaJob> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM landlord.schufa_job as s WHERE s.customer_id = ?#{principal.customer.id} " +
            "AND s.agent_info->>'id' = CAST(:userId AS varchar) ORDER BY ?#{#pageable}",
            countQuery = "SELECT COUNT(*) FROM landlord.schufa_job as s  WHERE s.customer_id = ?#{principal.customer.id} " +
                    "AND s.agent_info->>'id' = CAST(:userId AS varchar) ORDER BY ?#{#pageable}",
            nativeQuery = true)
    Page<LandlordSchufaJob> filterByAgent(@Param("userId") Long userId, Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends LandlordSchufaJob> S save(S entity);

    @Override
    @PreAuthorize("#schufaJob.customer.id == principal.customer.id")
    void delete(@Param("schufaJob") LandlordSchufaJob schufaJob);

    @RestResource(exported = false)
    @Query("SELECT count (o)  FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Long countByCustomer();

    @RestResource(exported = false)
    @Query("SELECT count (o)  FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id} AND o.userProfile.id = :userProfileId")
    Long countByUserProfile(@Param("userProfileId") Long userProfileId);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id} AND o.userProfile.id = :userProfileId order by created desc")
    Page<LandlordSchufaJob> findByUserProfile(Pageable pageable, @Param("userProfileId") Long userProfileId);

}
