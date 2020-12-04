package de.immomio.model.repository.core.landlord.customer.user.right;

import de.immomio.data.landlord.entity.user.right.LandlordRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Bastian Bliemeister
 */
public interface BaseLandlordRightRepository extends JpaRepository<LandlordRight, Long> {

    @Override
    @PreAuthorize("hasAuthority('is_admin')")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("hasAuthority('is_admin')")
    void delete(@Param("right") LandlordRight right);

    @Override
    @PreAuthorize("hasAuthority('is_admin')") <T extends LandlordRight> T save(@Param("right") T right);
}
