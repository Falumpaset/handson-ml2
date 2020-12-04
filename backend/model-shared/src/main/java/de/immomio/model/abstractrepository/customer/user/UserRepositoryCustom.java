/**
 *
 */
package de.immomio.model.abstractrepository.customer.user;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import org.springframework.data.repository.query.Param;

/**
 * @author Bastian Bliemeister.
 */
public interface UserRepositoryCustom<U extends AbstractUser> {

    U customSave(U user);

    U customSave(U user, boolean flush);

    void customDelete(U user);

    U customFindOne(@Param("id") Long id);

}
