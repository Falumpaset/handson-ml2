package de.immomio.service.mock.tenant;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.exception.ExternalApiNotFoundException;
import de.immomio.model.selfdisclosure.SdResponseApiBean;
import de.immomio.service.base.tenant.ExternalTenantInfoService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Service
public class ExternalTenantInfoServiceMock implements ExternalTenantInfoService {

    private static final List<Long> VALID_IDS = List.of(132422L, 132423L, 132424L);
    private static final Long NO_DOCUMENT_ID = 132424L;

    public static final String APPLICATION_ZIP = "application/zip";

    @Override
    public List<Long> getCreatedTenantIdsSince(Date since) {
        return VALID_IDS;
    }

    @Override
    public SdResponseApiBean getTenantInfo(Long propertyTenantId) throws IOException {
        if (!VALID_IDS.contains(propertyTenantId)) {
            throw new ExternalApiNotFoundException();
        }
        InputStream resourceAsStream = getClass().getResourceAsStream("/mock/selfdisclosure.json");
        ObjectMapper objectMapper = new ObjectMapper();
        SdResponseApiBean sdResponseApiBean = objectMapper.readValue(resourceAsStream, SdResponseApiBean.class);
        sdResponseApiBean.setId(String.valueOf(propertyTenantId));
        if (propertyTenantId.equals(NO_DOCUMENT_ID)) {
            sdResponseApiBean.setDocuments(Collections.emptyList());
        }
        return sdResponseApiBean;

    }

    @Override
    public byte[] getZipFileContent(Long propertyTenantId) throws IOException {
        if (!VALID_IDS.contains(propertyTenantId) || propertyTenantId.equals(NO_DOCUMENT_ID)) {
            throw new ExternalApiNotFoundException();
        }
        InputStream resourceAsStream = getClass().getResourceAsStream("/mock/documents.zip");

        return IOUtils.toByteArray(resourceAsStream);
    }
}
