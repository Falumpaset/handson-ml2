/**
 *
 */
package de.immomio.model.abstractrepository.customer;

import de.immomio.data.base.entity.customer.AbstractCustomer;
import org.springframework.data.repository.query.Param;

/**
 * @author Bastian Bliemeister
 */
public interface CustomerRepositoryCustom<C extends AbstractCustomer<?, ?>> {

    C customSave(C customer);

    C customFindOne(@Param("id") Long id);

    void customDelete(C Customer);

}
