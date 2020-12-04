package de.immomio.model.repository.service.landlord.customer.user.changepassword;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.changepassword.ChangePassword;
import de.immomio.model.abstractrepository.customer.user.changepassword.BaseAbstractChangePasswordRepository;

/**
 * @author Bastian Bliemeister
 */
public interface LandlordChangePasswordRepository
        extends BaseAbstractChangePasswordRepository<LandlordUser, ChangePassword> {

}
