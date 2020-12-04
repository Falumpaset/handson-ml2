package de.immomio.model.repository.landlord.customer.prioset;

import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.model.repository.core.landlord.customer.prioset.BasePriosetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Bastian Bliemeister
 */
@RepositoryRestResource(path = "priosets")
public interface LandlordPriosetRepository extends BasePriosetRepository, LandlordPriosetRepositoryCustom<Prioset> {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND o.customer.id = ?#{principal.customer.id}")
    Optional<Prioset> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<Prioset> findAll(Pageable pageable);

    @Override
    @PreAuthorize("#prioset.customer == principal?.customer")
    Prioset save(@Param("prioset") Prioset prioset);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#prioset.customer == principal?.customer")
    void delete(@Param("prioset") Prioset prioset);

    @Query("SELECT o FROM #{#entityName} o WHERE o.template = :template AND o.customer.id = ?#{principal.customer.id}")
    @RestResource(exported = false)
    Page<Prioset> findByTemplate(@Param("template") Boolean template, Pageable pageable);

}
