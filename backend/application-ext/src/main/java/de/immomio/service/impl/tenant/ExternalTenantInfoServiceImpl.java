package de.immomio.service.impl.tenant;

import de.immomio.data.base.entity.AbstractEntity;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.exception.ExternalApiNotFoundException;
import de.immomio.model.repository.shared.selfdisclosure.SelfDisclosureResponseRepository;
import de.immomio.model.repository.shared.tenant.PropertyTenantRepository;
import de.immomio.model.selfdisclosure.SdResponseApiBean;
import de.immomio.service.base.tenant.ExternalTenantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class ExternalTenantInfoServiceImpl implements ExternalTenantInfoService {
    private PropertyTenantRepository propertyTenantRepository;

    private SelfDisclosureResponseRepository selfDisclosureResponseRepository;

    private ExternalTenantInfoSdAnswerService externalTenantInfoSdAnswerService;

    private ExternalTenantInfoSdFileService externalTenantInfoSdFileService;

    @Autowired
    public ExternalTenantInfoServiceImpl(
            PropertyTenantRepository propertyTenantRepository,
            SelfDisclosureResponseRepository selfDisclosureResponseRepository,
            ExternalTenantInfoSdAnswerService externalTenantInfoSdAnswerService,
            ExternalTenantInfoSdFileService externalTenantInfoSdFileService
    ) {
        this.propertyTenantRepository = propertyTenantRepository;
        this.selfDisclosureResponseRepository = selfDisclosureResponseRepository;
        this.externalTenantInfoSdAnswerService = externalTenantInfoSdAnswerService;
        this.externalTenantInfoSdFileService = externalTenantInfoSdFileService;
    }

    @Override
    public List<Long> getCreatedTenantIdsSince(Date since) {
        if (since == null) {
            since = new Date(0);
        }
        List<PropertyTenant> allByCreatedAfter = propertyTenantRepository.findAllCreatedForExternalApi(since);
        return allByCreatedAfter.stream()
                .map(AbstractEntity::getId)
                .collect(Collectors.toList());
    }

    @Override
    public SdResponseApiBean getTenantInfo(Long propertyTenantId) throws IOException {
        PropertyTenant propertyTenant = propertyTenantRepository.findById(propertyTenantId).orElseThrow(ExternalApiNotFoundException::new);
        Optional<SelfDisclosureResponse> selfDisclosureResponse = selfDisclosureResponseRepository.findFirstByPropertyAndUserProfile(propertyTenant.getProperty(), propertyTenant.getUserProfile());
        SdResponseApiBean sdResponseApiBean;
        if (selfDisclosureResponse.isPresent()) {
            sdResponseApiBean = externalTenantInfoSdAnswerService.parseResponse(propertyTenant, selfDisclosureResponse.get());
        } else {
            sdResponseApiBean = externalTenantInfoSdAnswerService.parseResponse(propertyTenant);
        }
        return sdResponseApiBean;
    }

    @Override
    public byte[] getZipFileContent(Long propertyTenantId) throws IOException {
        PropertyTenant propertyTenant = propertyTenantRepository.findById(propertyTenantId).orElseThrow(ExternalApiNotFoundException::new);
        Optional<SelfDisclosureResponse> selfDisclosureResponse = selfDisclosureResponseRepository.findFirstByPropertyAndUserProfile(propertyTenant.getProperty(), propertyTenant.getUserProfile());
        ByteArrayOutputStream byteArrayOutputStream;
        if (selfDisclosureResponse.isPresent()) {
            byteArrayOutputStream = externalTenantInfoSdFileService.zipFilesToResponse(selfDisclosureResponse.get()).orElseThrow(ExternalApiNotFoundException::new);
        } else {
            byteArrayOutputStream = externalTenantInfoSdFileService.zipFilesToResponse(propertyTenant.getUserProfile()).orElseThrow(ExternalApiNotFoundException::new);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
