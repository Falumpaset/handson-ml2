package de.immomio.model.repository.landlord.customer.credential;

import com.sun.xml.bind.v2.model.core.ID;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.model.repository.core.landlord.customer.credential.BaseCredentialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

/**
 * @author Johannes Hiemer
 * @author Bastian Bliemeister
 */
public interface LandlordCredentialRepository extends BaseCredentialRepository, LandlordCredentialRepositoryCustom {

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND (o.customer.id = ?#{principal.customer.id})")
    Optional<Credential> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<Credential> findAll(Pageable pageable);

    @Override
    @PreAuthorize("#credential?.customer == principal?.customer") <T extends Credential> T save(
            @Param("credential") T credential);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#credential?.customer == principal?.customer")
    void delete(@Param("credential") Credential credential);

    @RestResource(exported = false)
    Credential findByCustomerAndPortal(LandlordCustomer customer, Portal portal);

    @Override
    @RestResource(exported = false)
    List<Credential> findAllById(Iterable<Long> ids);
}
