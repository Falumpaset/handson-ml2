package de.immomio.service.base.tenant;

import de.immomio.model.selfdisclosure.SdResponseApiBean;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
public interface ExternalTenantInfoService {
    List<Long> getCreatedTenantIdsSince(Date since);

    SdResponseApiBean getTenantInfo(Long propertyTenantId) throws IOException;

    byte[] getZipFileContent(Long propertyTenantId) throws IOException;
}
