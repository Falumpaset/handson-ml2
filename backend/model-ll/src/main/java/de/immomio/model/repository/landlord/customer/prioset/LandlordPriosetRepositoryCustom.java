/**
 *
 */
package de.immomio.model.repository.landlord.customer.prioset;

import de.immomio.data.landlord.entity.prioset.Prioset;
import org.springframework.data.repository.query.Param;

/**
 * @author Bastian Bliemeister.
 */
public interface LandlordPriosetRepositoryCustom<P extends Prioset> {

    P customSave(P user);

    P customFindOne(@Param("id") Long id);

}
