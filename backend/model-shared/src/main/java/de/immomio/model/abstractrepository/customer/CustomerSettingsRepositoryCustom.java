package de.immomio.model.abstractrepository.customer;

import de.immomio.data.base.entity.customer.AbstractCustomerSettings;

public interface CustomerSettingsRepositoryCustom<C extends AbstractCustomerSettings<?>> {

    C customSave(C customer);

}
