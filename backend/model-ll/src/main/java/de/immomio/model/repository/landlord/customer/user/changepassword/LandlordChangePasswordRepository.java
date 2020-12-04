package de.immomio.model.repository.landlord.customer.user.changepassword;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.model.abstractrepository.customer.user.changepassword.AbstractChangePasswordRepository;
import de.immomio.model.abstractrepository.customer.user.changepassword.ChangePasswordRepositoryCustom;

/**
 * @author Bastian Bliemeister
 */
public interface LandlordChangePasswordRepository
        extends AbstractChangePasswordRepository<LandlordUser, ChangePassword>,
        ChangePasswordRepositoryCustom<LandlordUser, ChangePassword> {

}
