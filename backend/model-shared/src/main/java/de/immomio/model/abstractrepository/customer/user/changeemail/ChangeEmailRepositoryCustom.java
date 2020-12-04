/**
 *
 */
package de.immomio.model.abstractrepository.customer.user.changeemail;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.base.entity.customer.user.changeemail.AbstractChangeEmail;
import org.springframework.data.repository.query.Param;

/**
 * @author Bastian Bliemeister.
 */
public interface ChangeEmailRepositoryCustom<U extends AbstractUser, T extends AbstractChangeEmail<U>> {

    T customSave(T changeEmail);

    T customFindOne(@Param("id") Long id);

    void customDelete(T changeEmail);
}
