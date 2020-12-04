package de.immomio.model.repository.core.landlord.customer;

import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.model.abstractrepository.customer.BaseAbstractCustomerSettingsRepository;

import java.util.List;

/**
 * @author Maik Kingma
 */
public interface BaseLandlordCustomerSettingsRepository extends BaseAbstractCustomerSettingsRepository<LandlordCustomerSettings> {

    List<LandlordCustomerSettings> findAllByThemeUrlIsNull();

}
