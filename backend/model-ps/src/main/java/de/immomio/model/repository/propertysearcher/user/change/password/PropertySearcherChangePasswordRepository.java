package de.immomio.model.repository.propertysearcher.user.change.password;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherChangePassword;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.model.abstractrepository.customer.user.changepassword.AbstractChangePasswordRepository;
import de.immomio.model.abstractrepository.customer.user.changepassword.ChangePasswordRepositoryCustom;

public interface PropertySearcherChangePasswordRepository extends AbstractChangePasswordRepository<PropertySearcherUser, PropertySearcherChangePassword>,
                                                                  ChangePasswordRepositoryCustom<PropertySearcherUser, PropertySearcherChangePassword> {
}
