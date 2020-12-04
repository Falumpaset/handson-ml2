package de.immomio.model.repository.core.landlord.customer.credential;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/**
 * @author Johannes Hiemer
 * @author Bastian Bliemeister
 */
public interface BaseCredentialRepository extends JpaRepository<Credential, Long> {

    @Override
    Optional<Credential> findById(@Param("id") Long id);

    @Override
    Page<Credential> findAll(Pageable pageable);

    @Override <T extends Credential> T save(@Param("credential") T credential);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("credential") Credential credential);

    @RestResource(exported = false)
    Credential findByCustomerAndPortal(LandlordCustomer customer, Portal portal);
}
