package de.immomio.model.repository.propertysearcher.user.change.email;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangeEmail;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.changeemail.AbstractChangeEmailRepository;
import de.immomio.model.abstractrepository.customer.user.changeemail.ChangeEmailRepositoryCustom;

public interface PropertySearcherChangeEmailRepository
        extends AbstractChangeEmailRepository<PropertySearcherUser, PropertySearcherChangeEmail>, ChangeEmailRepositoryCustom<PropertySearcherUser, PropertySearcherChangeEmail> {
}
