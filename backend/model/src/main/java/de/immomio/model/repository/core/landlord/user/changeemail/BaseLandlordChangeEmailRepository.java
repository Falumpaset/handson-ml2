package de.immomio.model.repository.core.landlord.user.changeemail;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.changeemail.ChangeEmail;
import de.immomio.model.abstractrepository.customer.user.changeemail.BaseAbstractChangeEmailRepository;

public interface BaseLandlordChangeEmailRepository
        extends BaseAbstractChangeEmailRepository<LandlordUser, ChangeEmail> {
}
