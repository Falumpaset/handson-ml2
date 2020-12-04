package de.immomio.model.repository.core.shared.right;

import de.immomio.data.base.entity.customer.user.right.Right;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public interface BaseRightRepository extends JpaRepository<Right, Long> {

    @Override
    @PreAuthorize("hasAuthority('is_admin')")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("hasAuthority('is_admin')")
    void delete(@Param("right") Right right);

    @Override
    @PreAuthorize("hasAuthority('is_admin')") <T extends Right> T save(@Param("right") T right);
}
