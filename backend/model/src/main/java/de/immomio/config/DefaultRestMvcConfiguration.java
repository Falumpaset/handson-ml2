/**
 *
 */
package de.immomio.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.immomio.data.landlord.entity.ftpaccess.FtpAccess;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.data.landlord.entity.invoice.LandlordInvoice;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.propertysearcher.entity.invoice.PropertySearcherInvoice;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * @author Bastian Bliemeister
 */

public class DefaultRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
        config.setMaxPageSize(1000);
        config.setDefaultPageSize(25);
        config.useHalAsDefaultJsonMediaType(true);
        config.exposeIdsFor(
                FtpAccess.class,
                ImportLog.class,
                LandlordInvoice.class,
                PropertySearcherInvoice.class,
                LandlordCustomerSettings.class,
                PropertySearcherUserProfile.class
        );
    }

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
