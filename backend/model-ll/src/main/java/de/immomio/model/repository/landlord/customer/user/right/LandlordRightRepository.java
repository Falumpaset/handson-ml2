/**
 *
 */
package de.immomio.model.repository.landlord.customer.user.right;

import de.immomio.data.landlord.entity.user.right.LandlordRight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Bastian Bliemeister
 */
public interface LandlordRightRepository extends JpaRepository<LandlordRight, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("right") LandlordRight right);

    @Override
    @RestResource(exported = false) <T extends LandlordRight> T save(@Param("right") T right);
}
