package de.immomio.controller.impl;

import de.immomio.controller.base.BaseExternalTenantController;
import de.immomio.service.impl.tenant.ExternalTenantInfoServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Tag(name = "Tenant Info")
@RequestMapping("/api/tenant")
@RestController
public class ExternalTenantController extends BaseExternalTenantController<ExternalTenantInfoServiceImpl> {

    @Autowired
    public ExternalTenantController(ExternalTenantInfoServiceImpl tenantInfoService) {
        super(tenantInfoService);
    }

}
