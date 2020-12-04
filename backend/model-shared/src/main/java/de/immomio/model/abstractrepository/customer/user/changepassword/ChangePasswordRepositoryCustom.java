/**
 *
 */
package de.immomio.model.abstractrepository.customer.user.changepassword;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.entity.customer.user.changepassword.AbstractChangePassword;
import org.springframework.data.repository.query.Param;

/**
 * @author Bastian Bliemeister.
 */
public interface ChangePasswordRepositoryCustom<U extends AbstractUser, T extends AbstractChangePassword<U>> {

    T customSave(T changePassword);

    T customFindOne(@Param("id") Long id);

    void customDelete(T changePassword);
}
