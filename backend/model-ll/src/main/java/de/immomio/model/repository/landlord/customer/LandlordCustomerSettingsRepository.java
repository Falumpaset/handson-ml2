package de.immomio.model.repository.landlord.customer;

import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.model.abstractrepository.customer.AbstractCustomerSettingsRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author Maik Kingma
 */
public interface LandlordCustomerSettingsRepository
        extends AbstractCustomerSettingsRepository<LandlordCustomerSettings> {

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @RestResource(exported = false)
    void delete(@Param("settings") LandlordCustomerSettings settings);

}
