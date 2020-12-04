package de.immomio.model.repository.landlord.customer.user.changeemail;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.model.abstractrepository.customer.user.changeemail.AbstractChangeEmailRepository;
import de.immomio.model.abstractrepository.customer.user.changeemail.ChangeEmailRepositoryCustom;

/**
 * @author Bastian Bliemeister
 */
public interface LandlordChangeEmailRepository extends AbstractChangeEmailRepository<LandlordUser, ChangeEmail>,
        ChangeEmailRepositoryCustom<LandlordUser, ChangeEmail> {

}
